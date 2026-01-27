package com.semihsahinoglu.fixture_service.client;

import com.semihsahinoglu.fixture_service.config.GlobalFeignConfig;
import com.semihsahinoglu.fixture_service.config.InternalFeignConfig;
import com.semihsahinoglu.fixture_service.dto.TeamResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "teams-service", configuration = {InternalFeignConfig.class, GlobalFeignConfig.class})
public interface TeamClient {

    @GetMapping("/internal/teams/{id}/exists")
    Boolean existsById(@PathVariable("id") Long id);

    @GetMapping("/internal/teams/{id}")
    TeamResponse findTeamById(@PathVariable("id") Long id);

}
