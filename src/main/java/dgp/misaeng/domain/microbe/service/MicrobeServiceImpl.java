package dgp.misaeng.domain.microbe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dgp.misaeng.domain.microbe.dto.reponse.MicrobeEnvironmentResDTO;
import dgp.misaeng.domain.microbe.dto.reponse.MicrobeInfoResDTO;
import dgp.misaeng.domain.microbe.dto.request.MicrobeRecordReqDTO;
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
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MicrobeServiceImpl implements MicrobeService {

    private final MicrobeRepository microbeRepository;
    private final S3ImageService s3ImageService;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;

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

        //json 파싱
        try {
            JsonNode jsonNode = objectMapper.readTree(latestData);
            float temperature = (float) jsonNode.get("temperature").asDouble();
            float humidity = (float) jsonNode.get("humidity").asDouble();

            //상태 계산
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

        List<String> todayData = redisService.getTodayMicrobeData(microbeId);
        String latestData = redisService.getLatestData(microbeId);

        // JSON 파싱
        JsonNode latestDataJson = parseJson(latestData);

        float latestWeight = (float) latestDataJson.get("weight").asDouble();
        String rgbStat = latestDataJson.get("rgb_stat").asText();
        List<String> foodCategories = objectMapper.convertValue(
                latestDataJson.get("food_category"),
                new TypeReference<>() {}
        );

        // 미생물 기본 정보 조회
        Microbe microbe = microbeRepository.findById(microbeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MICROBE) {});

        // MicrobeColor 계산
        MicrobeColor microbeColor = determineClosestColor(rgbStat);

        // 오늘 날짜의 총 음식 무게 계산
        float totalWeightToday = calculateTotalWeightToday(todayData);

        FoodWeightState foodWeightState = (totalWeightToday > 5.0f) ? FoodWeightState.FULL : FoodWeightState.GOOD;

        // Forbidden 상태 계산
        boolean forbidden = foodCategories.stream()
                .anyMatch(this::isForbiddenCategory);

        // MicrobeMessage 계산
        MicrobeMessage microbeMessage = determineMicrobeMessage(microbeId, foodWeightState, forbidden);

        // bDay 계산
        long bDay = ChronoUnit.DAYS.between(microbe.getCreatedAt().toLocalDate(), LocalDate.now());

        // MicrobeMood 계산
        MicrobeMood microbeMood = determineMicrobeMood(microbeMessage);

        // 결과 DTO 생성
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
            return objectMapper.readTree(data);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR) {
                @Override
                public ErrorCode getErrorCode() {
                    return super.getErrorCode();
                }
            };
        }
    }

    private float calculateTotalWeightToday(List<String> todayData) {
        return todayData.stream()
                .map(data -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(data);
                        return (float) jsonNode.get("weight").asDouble();
                    } catch (JsonProcessingException e) {
                        return 0f; // JSON 파싱 실패 시 기본값 반환
                    }
                })
                .reduce(0f, Float::sum);
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
}
