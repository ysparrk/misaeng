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
public class EnvironmentRedisConfig {

    @Value("${spring.redis2.host}")
    private String REDIS2_HOST;

    @Value("${spring.redis2.port}")
    private int REDIS2_PORT;

    @Value("${spring.redis2.password}")
    private String REDIS2_PW;

    @Primary
    @Bean(name = "EnvironmentRedisFactory")
    public RedisConnectionFactory environmentRedisFactory() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
        connectionFactory.setHostName(REDIS2_HOST);
        connectionFactory.setPort(REDIS2_PORT);
        connectionFactory.setPassword(REDIS2_PW);
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }

    @Bean(name = "EnvironmentRedis")
    public RedisTemplate<String, String> SubRedis(@Qualifier("EnvironmentRedisFactory") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    //TODO: redisTemplate이름 생성하지 않고 오류나지 않게 config 수정
    @Primary
    @Bean(name = "test")
    public RedisConnectionFactory test() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
        connectionFactory.setHostName(REDIS2_HOST);
        connectionFactory.setPort(REDIS2_PORT);
        connectionFactory.setPassword(REDIS2_PW);
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, String> redisTemplate(@Qualifier("test") RedisConnectionFactory test) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(test);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
