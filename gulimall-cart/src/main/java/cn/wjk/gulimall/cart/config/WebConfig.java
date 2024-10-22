package cn.wjk.gulimall.cart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Package: cn.wjk.gulimall.cart.config
 * @ClassName: WebConfig
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/22 下午6:47
 * @Description: MVC配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("success");
    }
}
