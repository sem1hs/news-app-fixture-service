package com.semihsahinoglu.fixture_service.config;

import com.semihsahinoglu.fixture_service.entity.FootballApiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FootballApiConfig {

    private final FootballApiProperties properties;

    public FootballApiConfig(FootballApiProperties properties) {
        this.properties = properties;
    }

    @Bean
    public WebClient footballWebClient() {
        ExchangeFilterFunction logFilter = (request, next) -> {
            System.out.println("Request: " + request.url());
            return next.exchange(request).doOnNext(response -> System.out.println("Response status: " + response.statusCode()));
        };

        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("x-apisports-key", properties.getKey())
                .filter(logFilter)
                .build();
    }
}
