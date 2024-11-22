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
public class MicrobeRedisConfig {
    @Value("${spring.redis3.host}")
    private String REDIS3_HOST;

    @Value("${spring.redis3.port}")
    private int REDIS3_PORT;

    @Value("${spring.redis3.password}")
    private String REDIS3_PW;

    @Primary
    @Bean(name = "MicrobeRedisFactory")
    public RedisConnectionFactory microbeRedisFactory() {
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory();
        connectionFactory.setHostName(REDIS3_HOST);
        connectionFactory.setPort(REDIS3_PORT);
        connectionFactory.setPassword(REDIS3_PW);
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }

    @Bean(name = "MicrobeRedis")
    public RedisTemplate<String, String> SubRedis(@Qualifier("MicrobeRedisFactory") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
