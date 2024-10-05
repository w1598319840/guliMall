package cn.wjk.gulimall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @Package: cn.wjk.gulimall.gateway.config
 * @ClassName: CorsConfig
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/5 下午1:38
 * @Description: 使用spring提供的跨域过滤器来解决跨域问题
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);//跨域时是否允许携带cookie
        corsConfiguration.addAllowedHeader("*");//允许哪些头跨域
        corsConfiguration.addAllowedMethod("*");//允许哪些请求方式跨域
        corsConfiguration.addAllowedOriginPattern("*");//允许哪些请求来源跨域
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }
}
