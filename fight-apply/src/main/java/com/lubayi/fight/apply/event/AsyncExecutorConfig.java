package com.lubayi.fight.apply.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lubayi
 * @date 2025/12/9
 */
@Configuration
@EnableAsync
public class AsyncExecutorConfig {

    @Bean("smsExecutor")
    public Executor smsExecutor() {
        int coreSize = Runtime.getRuntime().availableProcessors() * 2;
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(coreSize); // 核心线程数
        executor.setMaxPoolSize(coreSize * 2);  // 最大线程数
        executor.setQueueCapacity(500); // 队列容量
        executor.setKeepAliveSeconds(60);   // 空闲线程存活时间（秒）
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());    // 拒绝策略（调用者运行策略）
        executor.setThreadNamePrefix("Sms-Send-Executor-"); // 线程命名
        executor.initialize();
        return executor;
    }

}
