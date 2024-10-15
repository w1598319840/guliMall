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
    String PRODUCT_CATALOG_JSON_DATA_KEY = "product:catalog:json:data";
    Duration PRODUCT_CATALOG_JSON_DATA_EXPIRE_TIME = Duration.ofSeconds(60);
    String PRODUCT_CATALOG_JSON_LOCK_KEY = "product:catalog:json:lock";
    Duration PRODUCT_CATALOG_JSON_LOCK_EXPIRE_TIME = Duration.ofSeconds(5);
    String PRODUCT_CATEGORY_PREFIX = "product:category";
}
