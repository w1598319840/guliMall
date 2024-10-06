package cn.wjk.gulimall.thirdparty.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Package: cn.wjk.gulimall.thirdparty.config
 * @ClassName: OssConfig
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/5 下午7:38
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "gulimall.oss")
@Data
public class OssConfig {
    private String accessKey;
    private String secretKey;
    private String endpoint;
    private String bucket;
}
