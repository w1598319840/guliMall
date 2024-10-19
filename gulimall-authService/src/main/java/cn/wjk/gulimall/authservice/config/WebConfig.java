package cn.wjk.gulimall.authservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Package: cn.wjk.gulimall.authservice.config
 * @ClassName: WebConfig
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/18 下午4:55
 * @Description:
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/reg.html").setViewName("reg");

    }
}
