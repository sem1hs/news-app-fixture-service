package com.semihsahinoglu.fixture_service.dto;

import com.semihsahinoglu.fixture_service.entity.FixtureStatus;

import java.time.LocalDateTime;

public record FixtureResponse(
        Long id,
        Long leagueId,
        Integer week,
        LocalDateTime matchDate,
        Long homeTeamId,
        Long awayTeamId,
        Integer homeScore,
        Integer awayScore,
        String stadium,
        String season,
        FixtureStatus status
) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long id;
        private Long leagueId;
        private Integer week;
        private LocalDateTime matchDate;
        private Long homeTeamId;
        private Long awayTeamId;
        private Integer homeScore;
        private Integer awayScore;
        private String stadium;
        private String season;
        private FixtureStatus status;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder leagueId(Long leagueId) {
            this.leagueId = leagueId;
            return this;
        }

        public Builder week(Integer week) {
            this.week = week;
            return this;
        }

        public Builder matchDate(LocalDateTime matchDate) {
            this.matchDate = matchDate;
            return this;
        }

        public Builder homeTeamId(Long homeTeamId) {
            this.homeTeamId = homeTeamId;
            return this;
        }

        public Builder awayTeamId(Long awayTeamId) {
            this.awayTeamId = awayTeamId;
            return this;
        }

        public Builder homeScore(Integer homeScore) {
            this.homeScore = homeScore;
            return this;
        }

        public Builder awayScore(Integer awayScore) {
            this.awayScore = awayScore;
            return this;
        }

        public Builder stadium(String stadium) {
            this.stadium = stadium;
            return this;
        }

        public Builder season(String season) {
            this.season = season;
            return this;
        }

        public Builder status(FixtureStatus status) {
            this.status = status;
            return this;
        }

        public FixtureResponse build() {
            return new FixtureResponse(id, leagueId, week, matchDate, homeTeamId, awayTeamId, homeScore, awayScore, stadium, season, status);
        }
    }
}
