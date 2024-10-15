package cn.wjk.gulimall.product.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

/**
 * @Package: cn.wjk.gulimall.product.utils
 * @ClassName: RedisUtils
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/14 下午2:50
 * @Description:
 */
@AllArgsConstructor
public class RedisUtils {
    private StringRedisTemplate stringRedisTemplate;

    public void setCache(String key, Object value, Duration duration) {
        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(value), duration);
    }

    public <T> T getCacheObject(String key, TypeReference<T> typeReference) {
        String cache = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(cache)) {
            return null;
        }
        return JSON.parseObject(cache, typeReference);
    }
}
