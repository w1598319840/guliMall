package cn.wjk.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Package: cn.wjk.gulimall.common.config
 * @ClassName: RedissonConfig
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/15 上午9:02
 * @Description: Redisson的配置类
 */
@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redisson() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://172.30.60.231:6379").setDatabase(0);
        return Redisson.create(config);
    }
}
