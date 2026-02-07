package com.semihsahinoglu.fixture_service.dto;

import java.util.List;

public record FixtureWeekResponse(
        Integer week,
        List<FixtureResponse> fixtures
) {
}
