package dgp.misaeng.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class SecurityRedisConfig {

    @Value("${spring.redis1.host}")
    private String REDIS1_HOST;

    @Value("${spring.redis1.port}")
    private int REDIS1_PORT;

    @Value("${spring.redis1.password}")
    private String REDIS1_PW;

    @Primary
    @Bean(name = "SecurityRedisFactory")
    public RedisConnectionFactory securityRedisFactory() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
        connectionFactory.setHostName(REDIS1_HOST); // 첫 번째 Redis 서버 호스트
        connectionFactory.setPort(REDIS1_PORT); // 첫 번째 Redis 서버 포트
        connectionFactory.setPassword(REDIS1_PW);
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }

    @Bean(name = "SecurityRedis")
    public RedisTemplate<String, String> SubRedis(@Qualifier("SecurityRedisFactory") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
