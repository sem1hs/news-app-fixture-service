package com.semihsahinoglu.fixture_service.dto;

import com.semihsahinoglu.fixture_service.entity.FixtureStatus;

import java.time.LocalDateTime;

public record FixtureTodayResponse(
        Long id,
        String leagueName,
        Integer week,
        LocalDateTime matchDate,
        String homeTeamName,
        String awayTeamName,
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
        private String leagueName;
        private Integer week;
        private LocalDateTime matchDate;
        private String homeTeamName;
        private String awayTeamName;
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

        public Builder leagueName(String leagueName) {
            this.leagueName = leagueName;
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

        public Builder homeTeamName(String homeTeamName) {
            this.homeTeamName = homeTeamName;
            return this;
        }

        public Builder awayTeamName(String awayTeamName) {
            this.awayTeamName = awayTeamName;
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

        public FixtureTodayResponse build() {
            return new FixtureTodayResponse(id, leagueName, week, matchDate, homeTeamName, awayTeamName, homeScore, awayScore, stadium, season, status);
        }
    }
}
