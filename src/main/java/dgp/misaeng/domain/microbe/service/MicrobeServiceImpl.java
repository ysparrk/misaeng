package dgp.misaeng.domain.microbe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dgp.misaeng.domain.capsule.entity.Capsule;
import dgp.misaeng.domain.capsule.entity.CapsuleHistory;
import dgp.misaeng.domain.capsule.repository.CapsuleHistoryRepository;
import dgp.misaeng.domain.capsule.repository.CapsuleRepository;
import dgp.misaeng.domain.device.entity.Device;
import dgp.misaeng.domain.device.repository.DeviceRepository;
import dgp.misaeng.domain.microbe.dto.reponse.*;
import dgp.misaeng.domain.microbe.dto.request.MicrobeDetailUpdateReqDTO;
import dgp.misaeng.domain.microbe.dto.request.MicrobeNewReqDTO;
import dgp.misaeng.domain.microbe.dto.request.MicrobeRecordReqDTO;
import dgp.misaeng.domain.microbe.dto.request.MicrobeUpdateReqDTO;
import dgp.misaeng.domain.microbe.entity.Microbe;
import dgp.misaeng.domain.microbe.repository.MicrobeRepository;
import dgp.misaeng.global.exception.CustomException;
import dgp.misaeng.global.exception.ErrorCode;
import dgp.misaeng.global.service.RedisService;
import dgp.misaeng.global.service.S3ImageService;
import dgp.misaeng.global.util.enums.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MicrobeServiceImpl implements MicrobeService {

    private final MicrobeRepository microbeRepository;
    private final S3ImageService s3ImageService;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;
    private final DeviceRepository deviceRepository;
    private final CapsuleRepository capsuleRepository;
    private final CapsuleHistoryRepository capsuleHistoryRepository;

    @Override
    public void saveRecord(MicrobeRecordReqDTO microbeRecordReqDTO, MultipartFile image) {

        Long microbeId = microbeRepository.findMicrobeIdBySerialNum(microbeRecordReqDTO.getSerialNum()).orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MICROBE) {
            @Override
            public ErrorCode getErrorCode() {
                return super.getErrorCode();
            }
        });

        String imgUrl = s3ImageService.upload(image);

        redisService.saveMicrobeRecordData(microbeId, microbeRecordReqDTO, imgUrl);
    }

    @Override
    public MicrobeEnvironmentResDTO getEnvironment(Long microbeId) {
        String latestData = redisService.getLatestEnvironmentData(microbeId);

        // 데이터가 null이면 기본값으로 채운 DTO 반환
        if (latestData == null) {
            return MicrobeEnvironmentResDTO.builder()
                    .temperature(0)
                    .humidity(0)
                    .temperatureState(null)
                    .humidityState(null)
                    .build();
        }

        // JSON 파싱
        try {
            JsonNode jsonNode = objectMapper.readTree(latestData);
            Float temperature = (float) jsonNode.get("temperature").asDouble();
            Float humidity = (float) jsonNode.get("humidity").asDouble();

            // 상태 계산
            EnvironmentState temperatureState = determineTemperatureState(temperature);
            EnvironmentState humidityState = determineHumidityState(humidity);

            return MicrobeEnvironmentResDTO.builder()
                    .temperature(temperature)
                    .humidity(humidity)
                    .temperatureState(temperatureState)
                    .humidityState(humidityState)
                    .build();
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR) {
                @Override
                public ErrorCode getErrorCode() {
                    return super.getErrorCode();
                }
            };
        }
    }

    @Override
    public MicrobeInfoResDTO getMicrobeInfo(Long microbeId) {

        List<String> todayData = redisService.getTodayMicrobeData(microbeId);  //총 weight 계산용
        String latestData = redisService.getLatestData(microbeId);

        // 기본값
        MicrobeColor microbeColor = MicrobeColor.BLUE;
        MicrobeMood microbeMood = MicrobeMood.SMILE;
        MicrobeMessage microbeMessage = MicrobeMessage.GOOD;
        FoodWeightState foodWeightState = FoodWeightState.GOOD;
        float totalWeightToday = 0f;
        boolean forbidden = false;

        //비어있지 않다면
        if (latestData != null && !latestData.isEmpty()) {
            // JSON 파싱
            System.out.println("data " + latestData);


            JsonNode latestDataJson = parseJson(latestData);


            String rgbStat = latestDataJson.get("rgb_stat").asText();
            List<String> foodCategories = objectMapper.convertValue(
                    latestDataJson.get("food_category"),
                    new TypeReference<>() {}
            );

            // MicrobeColor 계산
            microbeColor = determineClosestColor(rgbStat);

            // 오늘 날짜의 총 음식 무게 계산
            totalWeightToday = calculateTotalWeightToday(todayData);

            // FoodWeightState 계산
            foodWeightState = (totalWeightToday > 5.0f) ? FoodWeightState.FULL : FoodWeightState.GOOD;

            // Forbidden 상태 계산
            forbidden = foodCategories.stream()
                    .anyMatch(this::isForbiddenCategory);

            // MicrobeMessage 계산
            microbeMessage = determineMicrobeMessage(microbeId, foodWeightState, forbidden);

        }

        // 미생물 기본 정보 조회
        Microbe microbe = microbeRepository.findById(microbeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MICROBE) {});

        long bDay = ChronoUnit.DAYS.between(microbe.getCreatedAt().toLocalDate(), LocalDate.now()) + 1;

        microbeMood = determineMicrobeMood(microbeMessage);


        return MicrobeInfoResDTO.builder()
                .microbeId(microbeId)
                .microbeName(microbe.getMicrobeName())
                .bDay((int) bDay)
                .microbeColor(microbeColor)
                .microbeMood(microbeMood)
                .microbeMessage(microbeMessage)
                .foodWeightState(foodWeightState)
                .weight(totalWeightToday)
                .forbidden(forbidden)
                .build();

    }

    @Transactional
    @Override
    public void saveMicrobe(Long deviceId, String microbeName) {
        //새로운 미생물 등록
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_DEVICE) {
                    @Override
                    public ErrorCode getErrorCode() {
                        return super.getErrorCode();
                    }
                });

        Microbe microbe = Microbe.builder()
                .device(device)
                .microbeName(microbeName)
                .survive(true)
                .isDeleted(false)
                .build();

        microbeRepository.save(microbe);

        //캡슐 초기 재고 생성
        for (CapsuleType type : CapsuleType.values()) {
            Capsule capsule = Capsule.builder()
                    .stock(10)
                    .capsuleType(type)
                    .microbe(microbe)
                    .build();

            Capsule savedCapsule = capsuleRepository.save(capsule);

            CapsuleHistory history = CapsuleHistory.builder()
                    .useCnt(10)
                    .capsule(savedCapsule)
                    .useState(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            capsuleHistoryRepository.save(history);
        }
    }

    @Transactional
    @Override
    public void updateMicrobe(MicrobeUpdateReqDTO microbeUpdateReqDTO) {

        Microbe microbe = microbeRepository.findById(microbeUpdateReqDTO.getMicrobeId())
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MICROBE) {
                    @Override
                    public ErrorCode getErrorCode() {
                        return super.getErrorCode();
                    }
                });

        microbe.setMicrobeName(microbeUpdateReqDTO.getMicrobeName());
    }

    @Transactional
    @Override
    public void deleteMicrobe(Long microbeId) {
        Microbe microbe = microbeRepository.findById(microbeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MICROBE) {
                    @Override
                    public ErrorCode getErrorCode() {
                        return super.getErrorCode();
                    }
                });

        microbe.setDeleted(true);
        microbe.setSurvive(false);

        microbeRepository.save(microbe);
    }

    @Transactional
    @Override
    public void expireMicrobe(Long microbeId) {
        Microbe microbe = microbeRepository.findById(microbeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MICROBE) {
                    @Override
                    public ErrorCode getErrorCode() {
                        return super.getErrorCode();
                    }
                });

        microbe.setSurvive(false);
        microbeRepository.save(microbe);
    }

    @Override
    public List<MicrobeYearMonthResDTO> getYearMonth(Long microbeId, YearMonth yearMonth) {

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<MicrobeYearMonthResDTO> result = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

            List<String> dailyData = redisService.getMicrobeDataForDate(microbeId, date);

            // 금지 음식 체크
            boolean isForbidden = dailyData.stream()
                    .anyMatch(record -> extractFoodCategories(record).stream()
                            .anyMatch(this::isForbiddenCategory));

            // 자리 비움 체크
            boolean isEmpty = dailyData.stream()
                    .allMatch(this::isEmptyRecord);

            if (isForbidden) {
                result.add(new MicrobeYearMonthResDTO(date, MicrobeState.FORBIDDEN));
            } else if (isEmpty) {
                result.add(new MicrobeYearMonthResDTO(date, MicrobeState.EMPTY));
            }
        }

        return result;
    }

    @Override
    public MicrobeDateResDTO getDateDetails(Long microbeId, LocalDate localDate) {
        List<String> microbeData = redisService.getMicrobeDataForDate(microbeId, localDate);

        List<MicrobeDetailResDTO> detailList = microbeData.stream()
                .map(this::mapToMicrobeDetailResDTO)
                .collect(Collectors.toList());

        return MicrobeDateResDTO.builder()
                .date(localDate)
                .detailList(detailList)
                .build();
    }

    @Override
    public void updateDateDetails(MicrobeDetailUpdateReqDTO microbeDetailUpdateReqDTO) {

        // 데이터 조회
        Long microbeId = microbeDetailUpdateReqDTO.getMicrobeId();
        Long timestamp = microbeDetailUpdateReqDTO.getTimestamp();
        String key = "microbe:" + microbeId;

        Set<String> dataSet = redisService.getDataByTimestamp(microbeId, timestamp);

        if (dataSet.isEmpty()) {
            throw new CustomException(ErrorCode.NO_RECORD_FOUND) {
                @Override
                public ErrorCode getErrorCode() {
                    return super.getErrorCode();
                }
            };
        }

        //업데이트
        String originalData = dataSet.iterator().next();
        try {
            JsonNode originalNode = parseJson(originalData);

            ArrayNode updatedFoodCategory = objectMapper.valueToTree(microbeDetailUpdateReqDTO.getFoodCategory());
            ((ObjectNode) originalNode).set("food_category", updatedFoodCategory);

            String updatedData = objectMapper.writeValueAsString(originalNode);
            redisService.updateMicrobeData(key, timestamp, originalData, updatedData);

        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR) {
                @Override
                public ErrorCode getErrorCode() {
                    return super.getErrorCode();
                }
            };
        }
    }

    @Override
    public MicrobeFeedbackResDTO getFeedback(String serialNum, LocalDate date) {

        Microbe microbe = microbeRepository.findByDeviceSerialNum(serialNum).orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MICROBE) {
            @Override
            public ErrorCode getErrorCode() {
                return super.getErrorCode();
            }
        });

        System.out.println("조회할 미생물 번호 " + microbe.getMicrobeId());
        List<String> microbeData = redisService.getMicrobeDataForDate(microbe.getMicrobeId(), date);

        List<MicrobeFeedbackDetailResDTO> detailList = microbeData.stream()
                .map(this::mapToMicrobeFeedbackDetailResDTO)
                .collect(Collectors.toList());

        MicrobeFeedbackResDTO microbeFeedbackResDTO = MicrobeFeedbackResDTO.builder()
                .date(date)
                .dataList(detailList)
                .build();

        return microbeFeedbackResDTO;
    }

    // 온도 상태 계산
    private EnvironmentState determineTemperatureState(float temperature) {
        if (temperature >= 20 && temperature <= 40) {
            return EnvironmentState.GOOD;
        } else if (temperature > 40) {
            return EnvironmentState.HIGH;
        } else {
            return EnvironmentState.LOW;
        }
    }

    // 습도 상태 계산
    private EnvironmentState determineHumidityState(float humidity) {
        if (humidity >= 30 && humidity <= 70) {
            return EnvironmentState.GOOD;
        } else if (humidity > 70) {
            return EnvironmentState.HIGH;
        } else {
            return EnvironmentState.LOW;
        }
    }

    private JsonNode parseJson(String data) {
        try {
            // JSON 표준에 맞지 않는 배열 값 처리
            String sanitizedData = sanitizeJson(data);
            return objectMapper.readTree(sanitizedData);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR) {
                @Override
                public ErrorCode getErrorCode() {
                    return super.getErrorCode();
                }
            };
        }
    }


    private String sanitizeJson(String data) {
        // 정규식을 사용하여 JSON 데이터를 변환
        Pattern pattern = Pattern.compile("\\[([A-Za-z_\\s]+(?:,\\s?[A-Za-z_\\s]+)*)\\]");
        Matcher matcher = pattern.matcher(data);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String matchedGroup = matcher.group(1);

            // 빈 배열일 경우 null로 변환
            if (matchedGroup == null || matchedGroup.trim().isEmpty()) {
                matcher.appendReplacement(result, "null");
            } else {
                // 배열의 내용을 문자열로 감싸기
                String sanitizedGroup = matchedGroup.replaceAll("([A-Za-z_\\s]+)", "\"$1\"").replaceAll(",\\s+", "\", \"");
                matcher.appendReplacement(result, "[" + sanitizedGroup + "]");
            }
        }
        matcher.appendTail(result);

        return result.toString();
    }

    private float calculateTotalWeightToday(List<String> todayData) {
        float totalWeightToday = 0f;

        if (todayData != null && !todayData.isEmpty()) {
            for (String data : todayData) {
                JsonNode jsonNode = parseJson(data);
                float weight = (float) jsonNode.get("weight").asDouble();
                totalWeightToday += weight;
            }
        }

        return Math.round(totalWeightToday * 10) / 10.0f;
    }

    private MicrobeColor determineClosestColor(String rgbStat) {
        String[] rgbValues = rgbStat.split(",");
        int red = Integer.parseInt(rgbValues[0].trim());
        int green = Integer.parseInt(rgbValues[1].trim());
        int blue = Integer.parseInt(rgbValues[2].trim());

        return Arrays.stream(MicrobeColor.values())
                .min(Comparator.comparingDouble(color -> calculateColorDistance(color, red, green, blue)))
                .orElse(MicrobeColor.BLACK);
    }

    private double calculateColorDistance(MicrobeColor color, int red, int green, int blue) {
        return Math.sqrt(Math.pow(color.getRed() - red, 2)
                + Math.pow(color.getGreen() - green, 2)
                + Math.pow(color.getBlue() - blue, 2));
    }

    private boolean isForbiddenCategory(String category) {
        return List.of("KIMCHI", "STIR_FRIED", "FRIED").contains(category);
    }


    private MicrobeMessage determineMicrobeMessage(Long microbeId, FoodWeightState foodWeightState, boolean forbidden) {
        // 환경 상태 조회
        MicrobeEnvironmentResDTO environment = getEnvironment(microbeId);

        // BAD 조건: 온도/습도 중 하나라도 정상이 아닐 경우
        boolean isEnvironmentBad = environment.getTemperatureState() != EnvironmentState.GOOD
                || environment.getHumidityState() != EnvironmentState.GOOD;

        if (forbidden) return MicrobeMessage.DANGER;
        if (foodWeightState == FoodWeightState.FULL) return MicrobeMessage.FULL;
        if (isEnvironmentBad) return MicrobeMessage.BAD;
        return MicrobeMessage.GOOD;
    }

    private MicrobeMood determineMicrobeMood(MicrobeMessage microbeMessage) {
        return microbeMessage == MicrobeMessage.GOOD ? MicrobeMood.SMILE : MicrobeMood.BAD;
    }

    private List<String> extractFoodCategories(String recordJson) {
        try {
            JsonNode recordNode = parseJson(recordJson);

            if (recordNode.has("food_category") && recordNode.get("food_category").isArray()) {
                return objectMapper.convertValue(
                        recordNode.get("food_category"), new TypeReference<List<String>>() {}
                );
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            System.err.println("JSON 파싱 실패 - 원본 데이터: " + recordJson);
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR) {
                @Override
                public ErrorCode getErrorCode() {
                    return super.getErrorCode();
                }
            };
        }
    }

    private boolean isEmptyRecord(String recordJson) {
        try {
            JsonNode recordNode = parseJson(recordJson);

            if (recordNode.has("is_empty") && !recordNode.get("is_empty").isNull()) {
                return recordNode.get("is_empty").asBoolean();
            }

            return false;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR) {
                @Override
                public ErrorCode getErrorCode() {
                    return super.getErrorCode();
                }
            };
        }
    }

    private MicrobeDetailResDTO mapToMicrobeDetailResDTO(String recordJson) {
        try {
            // JSON 데이터 정리 및 파싱
            JsonNode recordNode = parseJson(recordJson);

            String createdAt = recordNode.has("created_at") ? recordNode.get("created_at").asText() : null;
            LocalTime time = (createdAt != null)
                    ? LocalDateTime.parse(createdAt, DateTimeFormatter.ISO_DATE_TIME).toLocalTime() // hh:mm 추출
                    : null;

            List<String> foodCategories = recordNode.has("food_category") && !recordNode.get("food_category").isNull()
                    ? objectMapper.convertValue(recordNode.get("food_category"), new TypeReference<List<String>>() {})
                    : Collections.emptyList();

            float weight = recordNode.has("weight") ? (float) recordNode.get("weight").asDouble() : 0f;
            String imgUrl = recordNode.has("img_url") ? recordNode.get("img_url").asText() : "";
            boolean isForbidden = foodCategories.stream().anyMatch(this::isForbiddenCategory);

            // CalendarState 결정
            MicrobeState calendarState = isForbidden
                    ? MicrobeState.FORBIDDEN
                    : MicrobeState.COMPLETE;

            return MicrobeDetailResDTO.builder()
                    .time(time != null ? time.format(DateTimeFormatter.ofPattern("HH:mm")) : null)
                    .calendarState(calendarState)
                    .foodCategory(foodCategories)
                    .weight(weight)
                    .imgUrl(imgUrl)
                    .timestamp(recordNode.has("timestamp") ? recordNode.get("timestamp").asText() : "")
                    .build();

        } catch (Exception e) {
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR) {
                @Override
                public ErrorCode getErrorCode() {
                    return super.getErrorCode();
                }
            };
        }
    }

    private MicrobeFeedbackDetailResDTO mapToMicrobeFeedbackDetailResDTO(String recordJson) {
        try {
            // JSON 데이터 정리 및 파싱
            JsonNode recordNode = parseJson(recordJson);

            String createdAtStr = recordNode.has("created_at") ? recordNode.get("created_at").asText() : null;
            LocalDateTime createdAt = (createdAtStr != null)
                    ? LocalDateTime.parse(createdAtStr, DateTimeFormatter.ISO_DATE_TIME)
                    : null;

            List<String> foodCategories = recordNode.has("food_category") && !recordNode.get("food_category").isNull()
                    ? objectMapper.convertValue(recordNode.get("food_category"), new TypeReference<List<String>>() {
            })
                    : Collections.emptyList();

            String imgUrl = recordNode.has("img_url") ? recordNode.get("img_url").asText() : "";

            return MicrobeFeedbackDetailResDTO.builder()
                    .foodCategory(foodCategories)
                    .imgUrl(imgUrl)
                    .createdAt(createdAt)
                    .build();

        } catch (Exception e) {
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR) {
                @Override
                public ErrorCode getErrorCode() {
                    return super.getErrorCode();
                }
            };
        }
    }
}
