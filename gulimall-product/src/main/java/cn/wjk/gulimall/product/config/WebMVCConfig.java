package cn.wjk.gulimall.product.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Package: cn.wjk.gulimall.product.config
 * @ClassName: WebMVCConfig
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/13 下午2:01
 * @Description:
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}
