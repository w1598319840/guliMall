package cn.wjk.gulimall.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "cn.wjk.gulimall")
@MapperScan("cn.wjk.gulimall.ware.dao")
@EnableTransactionManagement
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "cn.wjk.gulimall.common.feign")
public class GulimallWareApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallWareApplication.class, args);
    }
}
