package com.semihsahinoglu.fixture_service.dto;

public record StandingUpdateFromFixtureRequest(
        Long leagueId,
        Long homeTeamId,
        Long awayTeamId,
        int homeScore,
        int awayScore
) {
}
