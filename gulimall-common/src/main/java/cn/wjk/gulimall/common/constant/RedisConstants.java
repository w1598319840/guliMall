package cn.wjk.gulimall.common.constant;

import java.time.Duration;

/**
 * @Package: cn.wjk.gulimall.common.constant
 * @ClassName: RedisConstants
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/14 下午2:31
 * @Description: redis的常量类
 */
public interface RedisConstants {
    String PRODUCT_CATALOG_JSON = "product:catalog:json";
    Duration PRODUCT_CATALOG_JSON_EXPIRE_TIME = Duration.ofSeconds(60);
}
