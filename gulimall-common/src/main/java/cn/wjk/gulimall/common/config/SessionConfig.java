package cn.wjk.gulimall.common.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @Package: cn.wjk.gulimall.authservice.config
 * @ClassName: SessionConfig
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/22 下午1:25
 * @Description: Spring Session的配置类
 */
@Configuration
public class SessionConfig {
    /**
     * 修改session的作用域
     */
    @Bean
    @ConditionalOnClass(AbstractHttpSessionApplicationInitializer.class)
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        defaultCookieSerializer.setDomainName("gulimall.com");
        defaultCookieSerializer.setCookieName("GULISESSION");
        return defaultCookieSerializer;
    }

    /**
     * 修改session的序列化方式
     * Bean的名称必须为springSessionDefaultRedisSerializer
     */
    @Bean("springSessionDefaultRedisSerializer")
    public RedisSerializer<Object> redisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}
