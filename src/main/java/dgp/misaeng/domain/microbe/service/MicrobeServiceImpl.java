package dgp.misaeng.domain.microbe.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dgp.misaeng.domain.microbe.dto.reponse.MicrobeEnvironmentResDTO;
import dgp.misaeng.domain.microbe.dto.request.MicrobeRecordReqDTO;
import dgp.misaeng.domain.microbe.repository.MicrobeRepository;
import dgp.misaeng.global.exception.CustomException;
import dgp.misaeng.global.exception.ErrorCode;
import dgp.misaeng.global.service.RedisService;
import dgp.misaeng.global.service.S3ImageService;
import dgp.misaeng.global.util.enums.EnvironmentState;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
}
