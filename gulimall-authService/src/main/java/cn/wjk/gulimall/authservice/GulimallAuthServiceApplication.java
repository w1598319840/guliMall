package cn.wjk.gulimall.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("cn.wjk.gulimall.common.feign")
@SpringBootApplication(scanBasePackages = "cn.wjk.gulimall")
@EnableDiscoveryClient
public class GulimallAuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallAuthServiceApplication.class, args);
    }
}
