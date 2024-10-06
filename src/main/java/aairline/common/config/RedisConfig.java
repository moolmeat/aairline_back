package aairline.common.config;

import aairline.auction.entity.Auction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Auction> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Auction> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key Serializer - String type
        template.setKeySerializer(new StringRedisSerializer());

        // Value Serializer - JSON serialization using Jackson
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // Hash Key Serializer (optional)
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.setEnableTransactionSupport(true);  // 트랜잭션 지원 설정 (필요 시)
        template.afterPropertiesSet();

        return template;
    }
}