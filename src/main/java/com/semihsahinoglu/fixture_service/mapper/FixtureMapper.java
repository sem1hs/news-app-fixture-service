package com.semihsahinoglu.fixture_service.mapper;

import com.semihsahinoglu.fixture_service.dto.CreateFixtureRequest;
import com.semihsahinoglu.fixture_service.dto.FixtureResponse;
import com.semihsahinoglu.fixture_service.dto.FixtureTodayResponse;
import com.semihsahinoglu.fixture_service.dto.UpdateFixtureRequest;
import com.semihsahinoglu.fixture_service.entity.Fixture;
import com.semihsahinoglu.fixture_service.entity.FixtureStatus;
import org.springframework.stereotype.Component;

@Component
public class FixtureMapper {

    public Fixture toEntity(CreateFixtureRequest request) {
        if (request == null) return null;

        return Fixture.builder()
                .leagueId(request.leagueId())
                .week(request.week())
                .matchDate(request.matchDate())
                .homeTeamId(request.homeTeamId())
                .awayTeamId(request.awayTeamId())
                .stadium(request.stadium())
                .season(request.season())
                .status(FixtureStatus.SCHEDULED)
                .build();
    }

    public FixtureResponse toDto(Fixture fixture) {
        if (fixture == null) return null;

        return FixtureResponse.builder()
                .id(fixture.getId())
                .leagueId(fixture.getLeagueId())
                .week(fixture.getWeek())
                .matchDate(fixture.getMatchDate())
                .homeTeamId(fixture.getHomeTeamId())
                .awayTeamId(fixture.getAwayTeamId())
                .homeScore(fixture.getHomeScore())
                .awayScore(fixture.getAwayScore())
                .stadium(fixture.getStadium())
                .season(fixture.getSeason())
                .status(fixture.getStatus())
                .build();
    }

    public FixtureTodayResponse toDto(Fixture fixture, String leagueName, String homeTeamName, String awayTeamName) {
        if (fixture == null) return null;

        return FixtureTodayResponse.builder()
                .id(fixture.getId())
                .leagueName(leagueName)
                .week(fixture.getWeek())
                .matchDate(fixture.getMatchDate())
                .homeTeamName(homeTeamName)
                .awayTeamName(awayTeamName)
                .homeScore(fixture.getHomeScore())
                .awayScore(fixture.getAwayScore())
                .stadium(fixture.getStadium())
                .season(fixture.getSeason())
                .status(fixture.getStatus())
                .build();
    }

    public void updateEntity(Fixture fixture, UpdateFixtureRequest updateFixtureRequest) {
        updateFixtureRequest.matchDate().ifPresent(fixture::setMatchDate);
        updateFixtureRequest.stadium().ifPresent(fixture::setStadium);
        updateFixtureRequest.season().ifPresent(fixture::setSeason);

        if (updateFixtureRequest.homeScore().isPresent() || updateFixtureRequest.awayScore().isPresent()) {
            Integer homeScore = updateFixtureRequest.homeScore().orElseThrow(() -> new IllegalStateException("Home score girildiyse away score da girilmelidir"));
            Integer awayScore = updateFixtureRequest.awayScore().orElseThrow(() -> new IllegalStateException("Away score girildiyse home score da girilmelidir"));
            fixture.setHomeScore(homeScore);
            fixture.setAwayScore(awayScore);
            fixture.setStatus(FixtureStatus.FINISHED);
        }

        updateFixtureRequest.status().ifPresent(status -> {
            if (status == FixtureStatus.FINISHED && (fixture.getHomeScore() == null || fixture.getAwayScore() == null)) {
                throw new IllegalStateException("Skor girilmeden maç FINISHED yapılamaz");
            }
            fixture.setStatus(status);
        });
    }

}
