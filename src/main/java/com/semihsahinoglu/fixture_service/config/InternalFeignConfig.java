package com.semihsahinoglu.fixture_service.config;

import com.semihsahinoglu.fixture_service.provider.ServiceTokenProvider;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class InternalFeignConfig {

    private final ServiceTokenProvider tokenProvider;

    public InternalFeignConfig(ServiceTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public RequestInterceptor internalAuthInterceptor() {
        return requestTemplate -> requestTemplate.header("Authorization", "Bearer " + tokenProvider.getToken());
    }

    @Bean(name = "apiExecutor")
    public Executor apiExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("api-async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
