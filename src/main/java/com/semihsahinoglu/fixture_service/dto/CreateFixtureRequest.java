package com.semihsahinoglu.fixture_service.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateFixtureRequest(
        @NotNull
        Long leagueId,

        @NotNull
        Integer week,

        @NotNull
        LocalDateTime matchDate,

        @NotNull
        Long homeTeamId,

        @NotNull
        Long awayTeamId,

        String stadium,
        String season
) {
}
