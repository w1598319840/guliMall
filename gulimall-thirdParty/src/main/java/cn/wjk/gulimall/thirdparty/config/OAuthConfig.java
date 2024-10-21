package cn.wjk.gulimall.thirdparty.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Package: cn.wjk.gulimall.authservice.config
 * @ClassName: OAuthConfig
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/21 下午1:32
 * @Description:
 */
@Configuration
@EnableConfigurationProperties(GithubOAuthConfig.class)
@RequiredArgsConstructor
@Data
public class OAuthConfig {
    private final GithubOAuthConfig githubOAuthConfig;
}
