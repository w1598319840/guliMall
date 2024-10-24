package cn.wjk.gulimall.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @Package: cn.wjk.gulimall.common.config
 * @ClassName: RedisConfig
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/19 下午2:25
 * @Description:
 */
@Configuration
public class RedisConfig {
    @Bean
    @ConditionalOnClass(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(factory);
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        stringRedisTemplate.setKeySerializer(RedisSerializer.string());
        stringRedisTemplate.setValueSerializer(serializer);
        return stringRedisTemplate;
    }
}
