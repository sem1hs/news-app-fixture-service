package com.semihsahinoglu.fixture_service.dto.api;

import java.util.List;

public record ApiFootballFixtureResponse(
        List<ApiFootballFixtureWrapper> response
) {
}