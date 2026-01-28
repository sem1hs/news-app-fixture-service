package com.semihsahinoglu.fixture_service.client;

import com.semihsahinoglu.fixture_service.config.GlobalFeignConfig;
import com.semihsahinoglu.fixture_service.config.InternalFeignConfig;
import com.semihsahinoglu.fixture_service.dto.StandingUpdateFromFixtureRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "standing-service", configuration = {InternalFeignConfig.class, GlobalFeignConfig.class})
public interface StandingClient {

    @PostMapping("/internal/standing/update-from-fixture")
    ResponseEntity<Void> updateFromFixture(@RequestBody StandingUpdateFromFixtureRequest request);
}
