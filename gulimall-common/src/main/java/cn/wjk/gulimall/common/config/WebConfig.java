package cn.wjk.gulimall.common.config;

import cn.wjk.gulimall.common.interceptors.UserInfoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Package: cn.wjk.gulimall.common.config
 * @ClassName: WebConfig
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/23 下午10:04
 * @Description: 添加拦截器
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInfoInterceptor());
    }
}
