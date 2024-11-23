package dgp.misaeng.global.service;

import dgp.misaeng.domain.microbe.dto.request.MicrobeEnvironmentReqDTO;
import dgp.misaeng.domain.microbe.dto.request.MicrobeRecordReqDTO;
import dgp.misaeng.domain.microbe.repository.MicrobeRepository;
import dgp.misaeng.global.exception.CustomException;
import dgp.misaeng.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;



@Service
public class RedisService {

    private final RedisTemplate<String, String> securityRedis;
    private final RedisTemplate<String, String> environmentRedis;
    private final RedisTemplate<String, String> microbeRedis;
    private final MicrobeRepository microbeRepository;


    @Autowired
    public RedisService(@Qualifier("SecurityRedis") RedisTemplate<String, String> securityRedis,
                        @Qualifier("EnvironmentRedis") RedisTemplate<String, String> environmentRedis,
                        @Qualifier("MicrobeRedis") RedisTemplate<String, String> microbeRedis,
                        MicrobeRepository microbeRepository) {
        this.securityRedis = securityRedis;
        this.environmentRedis = environmentRedis;
        this.microbeRedis = microbeRedis;
        this.microbeRepository = microbeRepository;
    }


    public void setRefreshToken(String id, String refreshToken){
        // key : accessToken, value : refreshToken
        securityRedis.opsForValue().set(id, refreshToken);
        //30일
        securityRedis.expire(id,30L, TimeUnit.DAYS);
    }

    public String getRefreshToken(String id){
        return securityRedis.opsForValue().get(id);
    }


    public boolean deleteRefreshToken(String id){
        return Boolean.TRUE.equals(securityRedis.delete(id));
    }


    public void saveEnvironmentData(MicrobeEnvironmentReqDTO microbeEnvironmentReqDTO) {
        Long microbeId = microbeRepository.findMicrobeIdBySerialNum(microbeEnvironmentReqDTO.getSerialNum()).orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MICROBE) {
            @Override
            public ErrorCode getErrorCode() {
                return super.getErrorCode();
            }
        });


        String key = "environment:" + microbeId;

        // 현재 시간 생성
        long timestamp = System.currentTimeMillis(); // 밀리초 단위 타임스탬프
        LocalDateTime createdAt = LocalDateTime.now();
        String createdAtString = createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String value = String.format(
                "{\"temperature\": %.2f, \"humidity\": %.2f, \"created_at\": \"%s\", \"timestamp\": %d}",
                microbeEnvironmentReqDTO.getTemperature(),
                microbeEnvironmentReqDTO.getHumidity(),
                createdAtString,
                timestamp
        );

        environmentRedis.opsForZSet().add(key, value, timestamp);
    }


    public void saveMicrobeRecordData(Long microbeId, MicrobeRecordReqDTO microbeRecordReqDTO, String imgUrl) {
        String key = "microbe:" + microbeId;

        // 현재 현재시간 생성
        long timestamp = System.currentTimeMillis();
        LocalDateTime createdAt = LocalDateTime.now();
        String createdAtString = createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // 저장할 데이터 생성 (JSON 문자열 형태로)
        StringBuilder valueBuilder = new StringBuilder();
        valueBuilder.append("{");
        valueBuilder.append("\"weight\": ").append(microbeRecordReqDTO.getWeight()).append(", ");
        valueBuilder.append("\"rgb_stat\": \"").append(microbeRecordReqDTO.getRgbStat()).append("\", ");
        valueBuilder.append("\"food_category\": ").append(
                microbeRecordReqDTO.getFoodCategory() != null ? microbeRecordReqDTO.getFoodCategory().toString() : "[]"
        ).append(", ");
        valueBuilder.append("\"created_at\": \"").append(createdAtString).append("\", ");
        valueBuilder.append("\"timestamp\": ").append(timestamp).append(", ");
        valueBuilder.append("\"img_url\": \"").append(imgUrl != null ? imgUrl : "").append("\", ");
        valueBuilder.append("\"microbe_soil_condition\": \"").append(
                microbeRecordReqDTO.getMicrobeSoilCondition() != null ? microbeRecordReqDTO.getMicrobeSoilCondition().toString() : ""
        ).append("\"");
        valueBuilder.append("}");

        String value = valueBuilder.toString();

        microbeRedis.opsForZSet().add(key, value, timestamp);
    }

    public String getLatestEnvironmentData(Long microbeId) {
        String key = "environment:" + microbeId;

        Set<String> latestData = environmentRedis.opsForZSet().reverseRange(key, 0, 0);

        if (latestData == null || latestData.isEmpty()) {
            throw new CustomException(ErrorCode.NO_ENVIRONMENT_DATA) {
                @Override
                public ErrorCode getErrorCode() {
                    return super.getErrorCode();
                }
            };
        }

        return latestData.iterator().next();
    }

    public List<String> getTodayMicrobeData(Long microbeId) {
        String key = "microbe:" + microbeId;

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        long startTimestamp = startOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endTimestamp = endOfDay.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        Set<String> todayData = microbeRedis.opsForZSet().rangeByScore(key, startTimestamp, endTimestamp);

        return todayData != null ? new ArrayList<>(todayData) : new ArrayList<>();
    }

    public String getLatestData(Long microbeId) {
        String key = "microbe:" + microbeId;

        // Redis ZSET에서 가장 최신 데이터 조회
        Set<String> latestDataSet = microbeRedis.opsForZSet().reverseRange(key, 0, 0);
        if (latestDataSet == null || latestDataSet.isEmpty()) {
            throw new CustomException(ErrorCode.NO_ENVIRONMENT_DATA) {
                @Override
                public ErrorCode getErrorCode() {
                    return super.getErrorCode();
                }
            };
        }
        return latestDataSet.iterator().next();
    }

}
