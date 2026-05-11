package com.semihsahinoglu.fixture_service.client;

import com.semihsahinoglu.fixture_service.dto.api.ApiFootballFixtureResponse;
import com.semihsahinoglu.fixture_service.exception.FixtureNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FootballFixtureApiClient {

    private final WebClient footballWebClient;

    public FootballFixtureApiClient(WebClient footballWebClient) {
        this.footballWebClient = footballWebClient;
    }

    public ApiFootballFixtureResponse getFixtures(Long leagueExternalId, int season, String round) {

        return footballWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/fixtures")
                        .queryParam("league", leagueExternalId)
                        .queryParam("season", season)
                        .queryParam("round", round)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new FixtureNotFoundException("Fikstür bulunamadı : " + leagueExternalId)))
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new RuntimeException("Football API currently unavailable")))
                .bodyToMono(ApiFootballFixtureResponse.class)
                .block();
    }
}
