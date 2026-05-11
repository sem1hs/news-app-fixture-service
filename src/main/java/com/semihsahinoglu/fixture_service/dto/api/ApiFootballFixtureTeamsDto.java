package com.semihsahinoglu.fixture_service.dto.api;

public record ApiFootballFixtureTeamsDto(
        ApiFootballSimpleTeamDto home,
        ApiFootballSimpleTeamDto away
) {
}