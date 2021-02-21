package com.karmanchik.chtotib_bot_rest_service.components;

import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@Log4j
public class BeanConfiguration {

    @Bean
    public Executor taskScheduleThreadExecutor() {
        log.info("Creating Async task Executor!");
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("ScheduleThread-");
        executor.initialize();
        return executor;
    }
}
