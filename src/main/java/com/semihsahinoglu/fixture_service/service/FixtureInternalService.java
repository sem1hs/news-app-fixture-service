package com.semihsahinoglu.fixture_service.service;

import com.semihsahinoglu.fixture_service.client.StandingClient;
import com.semihsahinoglu.fixture_service.dto.StandingUpdateFromFixtureRequest;
import com.semihsahinoglu.fixture_service.entity.Fixture;
import com.semihsahinoglu.fixture_service.entity.FixtureStatus;
import org.springframework.stereotype.Service;

@Service
public class FixtureInternalService {

    private final StandingClient standingClient;

    public FixtureInternalService(StandingClient standingClient) {
        this.standingClient = standingClient;
    }

    public void handleFixtureUpdate(Fixture fixture, FixtureStatus oldStatus) {

        boolean becameFinished = oldStatus != FixtureStatus.FINISHED && fixture.getStatus() == FixtureStatus.FINISHED;

        if (!becameFinished) return;

        if (fixture.getHomeScore() == null || fixture.getAwayScore() == null)
            throw new IllegalStateException("FINISHED maçta skor boş olamaz");

        StandingUpdateFromFixtureRequest standingUpdateFromFixtureRequest = new StandingUpdateFromFixtureRequest(fixture.getLeagueId(), fixture.getHomeTeamId(), fixture.getAwayTeamId(), fixture.getHomeScore(), fixture.getAwayScore());
        standingClient.updateFromFixture(standingUpdateFromFixtureRequest);
    }

    public void handleFixtureCreate(Fixture fixture) {

        if (fixture.getStatus() != FixtureStatus.FINISHED) return;

        if (fixture.getHomeScore() == null || fixture.getAwayScore() == null)
            throw new IllegalStateException("FINISHED maç skor olmadan eklenemez");

        StandingUpdateFromFixtureRequest standingUpdateFromFixtureRequest = new StandingUpdateFromFixtureRequest(fixture.getLeagueId(), fixture.getHomeTeamId(), fixture.getAwayTeamId(), fixture.getHomeScore(), fixture.getAwayScore());
        standingClient.updateFromFixture(standingUpdateFromFixtureRequest);
    }
}
