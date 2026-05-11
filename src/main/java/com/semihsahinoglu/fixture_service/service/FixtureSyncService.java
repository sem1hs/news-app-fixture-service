package com.semihsahinoglu.fixture_service.service;

import com.semihsahinoglu.fixture_service.client.FootballFixtureApiClient;
import com.semihsahinoglu.fixture_service.client.LeagueClient;
import com.semihsahinoglu.fixture_service.client.TeamClient;
import com.semihsahinoglu.fixture_service.dto.LeagueResponse;
import com.semihsahinoglu.fixture_service.dto.TeamResponse;
import com.semihsahinoglu.fixture_service.dto.api.*;
import com.semihsahinoglu.fixture_service.entity.Fixture;
import com.semihsahinoglu.fixture_service.mapper.FixtureMapper;
import com.semihsahinoglu.fixture_service.repository.FixtureRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FixtureSyncService {

    private final FixtureRepository fixtureRepository;
    private final FootballFixtureApiClient footballFixtureApiClient;
    private final TeamClient teamClient;
    private final LeagueClient leagueClient;
    private final FixtureMapper fixtureMapper;

    public FixtureSyncService(FixtureRepository fixtureRepository, FootballFixtureApiClient footballFixtureApiClient, TeamClient teamClient, LeagueClient leagueClient, FixtureMapper fixtureMapper) {
        this.fixtureRepository = fixtureRepository;
        this.footballFixtureApiClient = footballFixtureApiClient;
        this.teamClient = teamClient;
        this.leagueClient = leagueClient;
        this.fixtureMapper = fixtureMapper;
    }


    public List<Fixture> syncFixtures(Long leagueExternalId, int season, String round) {

        ApiFootballFixtureResponse response = footballFixtureApiClient.getFixtures(leagueExternalId, season, round);

        List<Fixture> fixtures = new ArrayList<>();

        for (ApiFootballFixtureWrapper wrapper : response.response()) {

            Long externalFixtureId = wrapper.fixture().id();

            LeagueResponse league = leagueClient.findLeagueExternalById(wrapper.league().id());

            TeamResponse homeTeam = teamClient.findTeamExternalById(wrapper.teams().home().id());

            TeamResponse awayTeam = teamClient.findTeamExternalById(wrapper.teams().away().id());

            Fixture fixture = fixtureMapper.toEntity(externalFixtureId, league, wrapper, homeTeam, awayTeam);

            Fixture savedFixture = fixtureRepository.save(fixture);

            fixtures.add(savedFixture);
        }

        return fixtures;
    }
}