package com.semihsahinoglu.fixture_service.dto.api;

public record ApiFootballFixtureWrapper(
        ApiFootballFixtureDto fixture,
        ApiFootballLeagueDto league,
        ApiFootballFixtureTeamsDto teams,
        ApiFootballGoalsDto goals
) {
}