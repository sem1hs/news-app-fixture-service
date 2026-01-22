package com.semihsahinoglu.fixture_service.config;

import com.semihsahinoglu.fixture_service.provider.ServiceTokenProvider;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}
