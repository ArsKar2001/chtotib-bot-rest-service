package com.karmanchik.chtotib_bot_rest_service.components;

import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Executor;

@Configuration
@EnableAsync
@Log4j
public class BeanConfiguration {

    @Bean
    public Executor taskScheduleThreadExecutor() {
        log.info("Creating Async task Executor!");
        return command -> {
            final ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) command;
            executor.setCorePoolSize(2);
            executor.setMaxPoolSize(2);
            executor.setQueueCapacity(100);
            executor.setThreadNamePrefix("ScheduleThread-");
            executor.initialize();
        };
    }

    @Bean
    public Filter filter() {
        log.info("Creating CORS Filter!");
        return (servletRequest, servletResponse, filterChain) -> {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
            filterChain.doFilter(servletRequest, servletResponse);
        };
    }
}
