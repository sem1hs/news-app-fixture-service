package com.semihsahinoglu.fixture_service.dto.api;

public record ApiFootballFixtureDto(
        Long id,
        String date,
        ApiFootballVenueDto venue,
        ApiFootballFixtureStatusDto status
) {
}