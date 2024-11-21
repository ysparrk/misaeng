package dgp.misaeng.global.service;

import dgp.misaeng.domain.environment.dto.request.EnvironmentReqDTO;
import dgp.misaeng.domain.food.dto.request.FoodReqDTO;
import dgp.misaeng.domain.microbe.repository.MicrobeRepository;
import dgp.misaeng.global.exception.CustomException;
import dgp.misaeng.global.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;



@Service
public class RedisService {

    private final RedisTemplate<String, String> securityRedis;
    private final RedisTemplate<String, String> environmentRedis;
    private final RedisTemplate<String, String> foodRedis;
    private final MicrobeRepository microbeRepository;


    @Autowired
    public RedisService(@Qualifier("SecurityRedis") RedisTemplate<String, String> securityRedis,
                        @Qualifier("EnvironmentRedis") RedisTemplate<String, String> environmentRedis,
                        @Qualifier("FoodRedis") RedisTemplate<String, String> foodRedis,
                        MicrobeRepository microbeRepository) {
        this.securityRedis = securityRedis;
        this.environmentRedis = environmentRedis;
        this.foodRedis = foodRedis;
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


    public void saveEnvironmentData(EnvironmentReqDTO environmentReqDTO) {
        Long microbeId = microbeRepository.findMicrobeIdBySerialNum(environmentReqDTO.getSericalNum()).orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MICROBE) {
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
                environmentReqDTO.getTemperature(),
                environmentReqDTO.getHumidity(),
                createdAtString,
                timestamp
        );

        environmentRedis.opsForZSet().add(key, value, timestamp);
    }


    public void saveFoodData(FoodReqDTO foodReqDTO) {
        Long microbeId = microbeRepository.findMicrobeIdBySerialNum(foodReqDTO.getSericalNum()).orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MICROBE) {
            @Override
            public ErrorCode getErrorCode() {
                return super.getErrorCode();
            }
        });

        String key = "microbe:" + microbeId;

        // 현재 시간 생성
        long timestamp = System.currentTimeMillis();
        LocalDateTime createdAt = LocalDateTime.now();
        String createdAtString = createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String value = String.format(
                "{\"weight\": %.2f, \"rgb_stat\": \"%s\", \"food_category\": %s, \"created_at\": \"%s\", \"timestamp\": %d}",
                foodReqDTO.getWeight(),
                foodReqDTO.getRgbStat(),
                foodReqDTO.getFoodCategory() != null ? foodReqDTO.getFoodCategory().toString() : "[]",
                createdAtString,
                timestamp
        );

        foodRedis.opsForZSet().add(key, value, timestamp);
    }

}
