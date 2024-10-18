package cn.wjk.gulimall.product.config;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Package: cn.wjk.gulimall.product.config
 * @ClassName: ThreadPoolConfig
 * @Version: 1.0
 * @Author: 温嘉凯
 * @Datetime: 2024/10/18 下午2:28
 * @Description: 项目中线程池的配置类
 */
@Configuration
@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
@RequiredArgsConstructor
public class ThreadPoolConfig {

    /**
     * 配置线程池
     */
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties properties) {
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger atomicInteger = new AtomicInteger(1);

            @Override
            public Thread newThread(@Nonnull Runnable runnable) {
                return new Thread(runnable, String.format("gulimall-product-thread-pool-%d",
                        atomicInteger.getAndIncrement()));
            }
        };
        return new ThreadPoolExecutor(properties.getCorePoolSize(),//核心线程数
                properties.getMaxPoolSize(),//最大线程数(核心线程数 + 救急线程数)
                properties.getKeepAliveTime().toMillis(),//救急线程空闲存活时间
                TimeUnit.MILLISECONDS,//单位
                new ArrayBlockingQueue<>(100000),//阻塞队列
                threadFactory,//线程工厂
                new ThreadPoolExecutor.AbortPolicy());//拒绝策略
    }
}
