package com.semihsahinoglu.fixture_service.client;

import com.semihsahinoglu.fixture_service.config.InternalFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "teams-service", url = "http://localhost:8083", configuration = InternalFeignConfig.class)
public interface TeamClient {

    @GetMapping("/internal/teams/{id}/exists")
    Boolean existsById(@PathVariable("id") Long id);

}
