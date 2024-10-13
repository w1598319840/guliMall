package cn.wjk.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Package: cn.wjk.gulimall.search.config
 * @ClassName: ElasticSearchConfig
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/12 下午8:02
 * @Description: ES的配置类
 */
@Configuration
public class ElasticSearchConfig {
    @Value("${gulimall.elasticsearch.host}")
    private String host;
    @Value("${gulimall.elasticsearch.port}")
    private Integer port;
    /**
     * 用于配置es
     */
    public static final RequestOptions COMMAND_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        COMMAND_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(host, port, HttpHost.DEFAULT_SCHEME_NAME))
        );
    }
}
