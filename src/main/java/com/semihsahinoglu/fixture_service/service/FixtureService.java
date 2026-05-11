package com.semihsahinoglu.fixture_service.service;

import com.semihsahinoglu.fixture_service.client.LeagueClient;
import com.semihsahinoglu.fixture_service.client.TeamClient;
import com.semihsahinoglu.fixture_service.dto.*;
import com.semihsahinoglu.fixture_service.entity.Fixture;
import com.semihsahinoglu.fixture_service.entity.FixtureStatus;
import com.semihsahinoglu.fixture_service.exception.FixtureNotFoundException;
import com.semihsahinoglu.fixture_service.exception.LeagueNotFoundException;
import com.semihsahinoglu.fixture_service.exception.TeamAlreadyHaveMatchException;
import com.semihsahinoglu.fixture_service.exception.TeamNotFoundException;
import com.semihsahinoglu.fixture_service.mapper.FixtureMapper;
import com.semihsahinoglu.fixture_service.repository.FixtureRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
public class FixtureService {

    private static final Logger log = LoggerFactory.getLogger(FixtureService.class);
    private final FixtureInternalService fixtureInternalService;
    private final FixtureRepository fixtureRepository;
    private final LeagueClient leagueClient;
    private final TeamClient teamClient;
    private final Executor apiExecutor;
    private final FixtureMapper fixtureMapper;
    private final FixtureSyncService fixtureSyncService;

    public FixtureService(FixtureInternalService fixtureInternalService, FixtureRepository fixtureRepository, LeagueClient leagueClient, TeamClient teamClient, @Qualifier("apiExecutor") Executor apiExecutor, FixtureMapper fixtureMapper, FixtureSyncService fixtureSyncService) {
        this.fixtureInternalService = fixtureInternalService;
        this.fixtureRepository = fixtureRepository;
        this.leagueClient = leagueClient;
        this.teamClient = teamClient;
        this.apiExecutor = apiExecutor;
        this.fixtureMapper = fixtureMapper;
        this.fixtureSyncService = fixtureSyncService;
    }

    public FixtureResponse getById(Long fixtureId) {
        Fixture fixture = fixtureRepository.findById(fixtureId).orElseThrow(() -> new FixtureNotFoundException("Fikstür bulunamadı " + fixtureId));
        return fixtureMapper.toDto(fixture);
    }

    public List<FixtureResponse> getByExternalId(Long leagueExternalId, int season, int start, int end) {
        List<List<Fixture>> fixtures = new ArrayList<>();

        for (int i = start; i <= end; i++) {
            String round = "Regular Season - " + i;
            List<Fixture> syncedFixtures = fixtureSyncService.syncFixtures(leagueExternalId, season, round);
            fixtures.add(syncedFixtures);
            syncedFixtures.forEach(fixtureInternalService::handleFixtureCreate);
        }
        return fixtures.stream().flatMap(List::stream).map(fixtureMapper::toDto).toList();
    }

    public List<FixtureWeekResponse> getAllByLeagueId(Long leagueId) {
        boolean leagueExist = leagueClient.existsById(leagueId);

        if (!leagueExist) throw new LeagueNotFoundException("Lig bulunamadı !");

        List<Fixture> fixtures = fixtureRepository.findByLeagueIdOrderByMatchDateAsc(leagueId).orElseThrow(() -> new FixtureNotFoundException("Fikstür bulunamadı !"));

        Map<Long, String> leagueCache = new HashMap<>();
        Map<Long, TeamResponse> teamCache = new HashMap<>();

        Map<Integer, List<FixtureTodayResponse>> groupedByWeek = fixtures.stream()
                .map(fixture -> {
                    String leagueName = leagueCache.computeIfAbsent(fixture.getLeagueId(), id -> leagueClient.findLeagueById(id).name());

                    TeamResponse homeTeam = teamCache.computeIfAbsent(fixture.getHomeTeamId(), teamClient::findTeamById);
                    TeamResponse awayTeam = teamCache.computeIfAbsent(fixture.getAwayTeamId(), teamClient::findTeamById);

                    return fixtureMapper.toDto(fixture, leagueName, homeTeam.name(), awayTeam.name(), homeTeam.logoUrl(), awayTeam.logoUrl());
                })
                .collect(Collectors.groupingBy(FixtureTodayResponse::week));

        return groupedByWeek.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new FixtureWeekResponse(entry.getKey(), entry.getValue()))
                .toList();
    }

    public List<FixtureTodayResponse> getByLeagueAndWeek(Long leagueId, Integer week) {
        boolean leagueExist = leagueClient.existsById(leagueId);

        if (!leagueExist) throw new LeagueNotFoundException("Lig bulunamadı !");

        List<Fixture> fixtures = fixtureRepository.findByLeagueIdAndWeekOrderByMatchDateAsc(leagueId, week).orElseThrow(() -> new FixtureNotFoundException("Fikstür bulunamadı !"));

        Map<Long, String> leagueCache = new HashMap<>();
        Map<Long, TeamResponse> teamCache = new HashMap<>();

        return fixtures.stream()
                .map(fixture -> {
                    String leagueName = leagueCache.computeIfAbsent(fixture.getLeagueId(), id -> leagueClient.findLeagueById(id).name());

                    TeamResponse homeTeam = teamCache.computeIfAbsent(fixture.getHomeTeamId(), teamClient::findTeamById);
                    TeamResponse awayTeam = teamCache.computeIfAbsent(fixture.getAwayTeamId(), teamClient::findTeamById);

                    return fixtureMapper.toDto(fixture, leagueName, homeTeam.name(), awayTeam.name(), homeTeam.logoUrl(), awayTeam.logoUrl());
                })
                .toList();
    }

    public Map<String, List<FixtureTodayResponse>> getTodayFixtures() {
        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        List<Fixture> fixtures = fixtureRepository.findTodayFixtures(startOfDay, endOfDay);

        if (fixtures == null) throw new FixtureNotFoundException("Bugüne ait fikstür bulunamadı !");

        Map<Long, String> leagueCache = new HashMap<>();
        Map<Long, TeamResponse> teamCache = new HashMap<>();

        return fixtures.stream()
                .map(fixture -> {
                    String leagueName = leagueCache.computeIfAbsent(fixture.getLeagueId(), id -> leagueClient.findLeagueById(id).name());

                    TeamResponse homeTeam = teamCache.computeIfAbsent(fixture.getHomeTeamId(), teamClient::findTeamById);
                    TeamResponse awayTeam = teamCache.computeIfAbsent(fixture.getAwayTeamId(), teamClient::findTeamById);

                    return fixtureMapper.toDto(fixture, leagueName, homeTeam.name(), awayTeam.name(), homeTeam.logoUrl(), awayTeam.logoUrl());
                })
                .collect(Collectors.groupingBy(
                        FixtureTodayResponse::leagueName,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }


    public FixtureResponse create(CreateFixtureRequest request) {
        if (request.homeTeamId().equals(request.awayTeamId())) {
            throw new IllegalArgumentException("Ev sahibi takım ve deplasman takımı aynı olamaz");
        }

        CompletableFuture<Boolean> leagueExistsFuture = CompletableFuture.supplyAsync(() -> leagueClient.existsById(request.leagueId()), apiExecutor);
        CompletableFuture<Boolean> homeExistsFuture = CompletableFuture.supplyAsync(() -> teamClient.existsById(request.homeTeamId()), apiExecutor);
        CompletableFuture<Boolean> awayExistsFuture = CompletableFuture.supplyAsync(() -> teamClient.existsById(request.awayTeamId()), apiExecutor);

        CompletableFuture.allOf(homeExistsFuture, awayExistsFuture, leagueExistsFuture).join();

        boolean homeExists = homeExistsFuture.join();
        boolean awayExists = awayExistsFuture.join();
        boolean leagueExist = leagueExistsFuture.join();

        if (!leagueExist) throw new LeagueNotFoundException("Lig bulunamadı !");

        if (!homeExists || !awayExists)
            throw new TeamNotFoundException("Ev sahibi takım veya deplasman takımı bulunamadı !");

        boolean homeTeamBusy = fixtureRepository.existsTeamInWeek(request.leagueId(), request.week(), request.homeTeamId());
        boolean awayTeamBusy = fixtureRepository.existsTeamInWeek(request.leagueId(), request.week(), request.awayTeamId());

        if (homeTeamBusy || awayTeamBusy)
            throw new TeamAlreadyHaveMatchException("Takımlardan biri bu ligde bu haftada zaten maç yapıyor !");


        Fixture fixture = fixtureMapper.toEntity(request);
        Fixture savedFixture = fixtureRepository.save(fixture);

        fixtureInternalService.handleFixtureCreate(savedFixture);

        return fixtureMapper.toDto(savedFixture);
    }

    @Transactional
    public List<FixtureResponse> createFixtureBulk(List<List<CreateFixtureRequest>> requestList) {
        return requestList.stream()
                .flatMap(List::stream)
                .map(this::create)
                .toList();
    }

    public FixtureResponse update(Long fixtureId, UpdateFixtureRequest request) {
        Fixture fixture = fixtureRepository.findById(fixtureId).orElseThrow(() -> new FixtureNotFoundException("Fikstür bulunamadı !"));

        FixtureStatus oldStatus = fixture.getStatus();

        fixtureMapper.updateEntity(fixture, request);
        Fixture updatedFixture = fixtureRepository.save(fixture);

        fixtureInternalService.handleFixtureUpdate(updatedFixture, oldStatus);

        return fixtureMapper.toDto(updatedFixture);
    }

    public void delete(Long id) {
        Fixture fixture = fixtureRepository.findById(id).orElseThrow(() -> new FixtureNotFoundException("Fikstür bulunamadı " + id));
        fixtureRepository.delete(fixture);
    }
}
