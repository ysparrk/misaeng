package dgp.misaeng.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;



@Service
public class RedisService {

    private final RedisTemplate<String, String> securityRedis;

    @Autowired
    public RedisService(@Qualifier("SecurityRedis") RedisTemplate<String, String> securityRedis) {
        this.securityRedis = securityRedis;
    }

    /** 여기에 리프레쉬 관련 로직 넣으면 좋아용 **/
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


}
