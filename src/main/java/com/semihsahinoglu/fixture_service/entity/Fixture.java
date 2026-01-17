package com.semihsahinoglu.fixture_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "fixture")
public class Fixture extends Auditable {

    @NotNull
    @Column(name = "league_id", nullable = false)
    private Long leagueId;

    @NotNull
    @Column(nullable = false)
    private Integer week;

    @NotNull
    @Column(name = "match_date", nullable = false)
    private LocalDateTime matchDate;

    @NotNull
    @Column(name = "home_team_id", nullable = false)
    private Long homeTeamId;

    @NotNull
    @Column(name = "away_team_id", nullable = false)
    private Long awayTeamId;

    @Column(name = "home_score")
    private Integer homeScore;

    @Column(name = "away_score")
    private Integer awayScore;

    @NotNull
    @Column(nullable = false)
    private Boolean played = false;

    @Column(length = 100)
    private String stadium;

    @Column(length = 20)
    private String season;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private FixtureStatus status;

    protected Fixture() {
    }

    public Fixture(Long leagueId, Integer week, LocalDateTime matchDate, Long homeTeamId, Long awayTeamId, Integer homeScore, Integer awayScore, Boolean played, String stadium, String season, FixtureStatus status) {
        this.leagueId = leagueId;
        this.week = week;
        this.matchDate = matchDate;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.played = played;
        this.stadium = stadium;
        this.season = season;
        this.status = status;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long leagueId;
        private Integer week;
        private LocalDateTime matchDate;
        private Long homeTeamId;
        private Long awayTeamId;
        private Integer homeScore;
        private Integer awayScore;
        private Boolean played;
        private String stadium;
        private String season;
        private FixtureStatus status;

        private Builder() {
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

        public Builder played(Boolean played) {
            this.played = played;
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

        public Fixture build() {
            return new Fixture(leagueId, week, matchDate, homeTeamId, awayTeamId, homeScore, awayScore, played, stadium, season, status);
        }
    }


    public Long getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Long leagueId) {
        this.leagueId = leagueId;
    }

    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public Long getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(Long homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public Long getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(Long awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }

    public Boolean getPlayed() {
        return played;
    }

    public void setPlayed(Boolean played) {
        this.played = played;
    }

    public String getStadium() {
        return stadium;
    }

    public void setStadium(String stadium) {
        this.stadium = stadium;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public FixtureStatus getStatus() {
        return status;
    }

    public void setStatus(FixtureStatus status) {
        this.status = status;
    }
}
