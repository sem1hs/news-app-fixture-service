package com.semihsahinoglu.fixture_service.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiFootballFixtureStatusDto(

        @JsonProperty("short")
        String shortCode

) {
}