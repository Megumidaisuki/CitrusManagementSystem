package com.feidian.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {
    @Bean
    public Executor asyncExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(0); //设置线程池的基本大小为2，即使线程池中的线程空闲，线程池也会保持至少有这么多的线程
        threadPoolTaskExecutor.setMaxPoolSize(10); //设置线程池的最大大小为2，这表示即使排队任务的数量增加，线程池也只能增加到这个数量的线程
        threadPoolTaskExecutor.setQueueCapacity(5000); //设置线程池的队列容量为500。如果所有的线程都在忙，新来的任务会被放在队列里面，等待有线程空闲出来
        threadPoolTaskExecutor.setKeepAliveSeconds(9999999);
        threadPoolTaskExecutor.initialize(); //初始化线程池
        return threadPoolTaskExecutor;
    }
}
