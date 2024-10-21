package cn.wjk.gulimall.thirdparty.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Package: cn.wjk.gulimall.authservice.config
 * @ClassName: GithubOAuthConfig
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/21 下午1:32
 * @Description:
 */
@ConfigurationProperties(prefix = "gulimall.oauth.github")
@Data
public class GithubOAuthConfig {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
