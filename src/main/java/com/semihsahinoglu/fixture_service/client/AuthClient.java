package com.semihsahinoglu.fixture_service.client;

import com.semihsahinoglu.fixture_service.config.GlobalFeignConfig;
import com.semihsahinoglu.fixture_service.config.NoAuthFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "auth-service", configuration = {NoAuthFeignConfig.class, GlobalFeignConfig.class})
public interface AuthClient {

    @PostMapping("/internal/auth/service-token")
    String getServiceToken();
}
