package com.semihsahinoglu.fixture_service.dto;

import com.semihsahinoglu.fixture_service.entity.FixtureStatus;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.util.Optional;

public record UpdateFixtureRequest(
        Optional<LocalDateTime> matchDate,
        Optional<String> stadium,
        Optional<String> season,

        @PositiveOrZero
        Optional<Integer> homeScore,
        @PositiveOrZero
        Optional<Integer> awayScore,

        Optional<FixtureStatus> status
) {
}
