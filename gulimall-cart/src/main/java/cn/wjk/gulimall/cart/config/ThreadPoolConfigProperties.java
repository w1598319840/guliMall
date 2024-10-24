package cn.wjk.gulimall.cart.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @Package: cn.wjk.gulimall.product.config
 * @ClassName: ThreadPoolConfigProperties
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/18 下午2:40
 * @Description:
 */
@ConfigurationProperties(prefix = "gulimall.cart.thread")
@Data
public class ThreadPoolConfigProperties {
    private int corePoolSize;
    private int maxPoolSize;
    private Duration keepAliveTime;
}
