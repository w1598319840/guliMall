package cn.wjk.gulimall.product.config;

import cn.wjk.gulimall.product.utils.RedisUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Package: cn.wjk.gulimall.product.config
 * @ClassName: RedisUtilsConfig
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/14 下午2:50
 * @Description: RedisUtils的配置类
 */
@Configuration
public class RedisUtilsConfig {
    @Bean("redisUtils")
    public RedisUtils redisUtils(StringRedisTemplate stringRedisTemplate) {
        return new RedisUtils(stringRedisTemplate);
    }
}
