package com.semihsahinoglu.fixture_service.service;

import com.semihsahinoglu.fixture_service.client.LeagueClient;
import com.semihsahinoglu.fixture_service.client.TeamClient;
import com.semihsahinoglu.fixture_service.dto.CreateFixtureRequest;
import com.semihsahinoglu.fixture_service.dto.FixtureResponse;
import com.semihsahinoglu.fixture_service.dto.FixtureTodayResponse;
import com.semihsahinoglu.fixture_service.dto.UpdateFixtureRequest;
import com.semihsahinoglu.fixture_service.entity.Fixture;
import com.semihsahinoglu.fixture_service.exception.FixtureNotFoundException;
import com.semihsahinoglu.fixture_service.exception.LeagueNotFoundException;
import com.semihsahinoglu.fixture_service.exception.TeamAlreadyHaveMatchException;
import com.semihsahinoglu.fixture_service.exception.TeamNotFoundException;
import com.semihsahinoglu.fixture_service.mapper.FixtureMapper;
import com.semihsahinoglu.fixture_service.repository.FixtureRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
public class FixtureService {

    private final FixtureRepository fixtureRepository;
    private final LeagueClient leagueClient;
    private final TeamClient teamClient;
    private final Executor apiExecutor;
    private final FixtureMapper fixtureMapper;


    public FixtureService(FixtureRepository fixtureRepository, LeagueClient leagueClient, TeamClient teamClient, @Qualifier("apiExecutor") Executor apiExecutor, FixtureMapper fixtureMapper) {
        this.fixtureRepository = fixtureRepository;
        this.leagueClient = leagueClient;
        this.teamClient = teamClient;
        this.apiExecutor = apiExecutor;
        this.fixtureMapper = fixtureMapper;
    }

    public FixtureResponse getById(Long fixtureId) {
        Fixture fixture = fixtureRepository.findById(fixtureId).orElseThrow(() -> new FixtureNotFoundException("Fikstür bulunamadı " + fixtureId));
        return fixtureMapper.toDto(fixture);
    }

    public List<FixtureResponse> getAllByLeagueId(Long leagueId) {
        boolean leagueExist = leagueClient.existsById(leagueId);

        if (!leagueExist) throw new LeagueNotFoundException("Lig bulunamadı !");

        List<Fixture> fixtures = fixtureRepository.findByLeagueIdOrderByMatchDateAsc(leagueId).orElseThrow(() -> new FixtureNotFoundException("Fikstür bulunamadı !"));

        return fixtures.stream().map(fixtureMapper::toDto).toList();
    }

    public List<FixtureResponse> getByLeagueAndWeek(Long leagueId, Integer week) {
        boolean leagueExist = leagueClient.existsById(leagueId);

        if (!leagueExist) throw new LeagueNotFoundException("Lig bulunamadı !");

        List<Fixture> fixtures = fixtureRepository.findByLeagueIdAndWeekOrderByMatchDateAsc(leagueId, week).orElseThrow(() -> new FixtureNotFoundException("Fikstür bulunamadı !"));

        return fixtures.stream().map(fixtureMapper::toDto).toList();
    }

    public Map<String, List<FixtureTodayResponse>> getTodayFixtures() {
        LocalDate today = LocalDate.now();

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        List<Fixture> fixtures = fixtureRepository.findTodayFixtures(startOfDay, endOfDay);

        if (fixtures == null) throw new FixtureNotFoundException("Bugüne ait fikstür bulunamadı !");

        Map<Long, String> leagueNameCache = new HashMap<>();
        Map<Long, String> teamNameCache = new HashMap<>();

        return fixtures.stream()
                .map(fixture -> {
                    String leagueName = leagueNameCache.computeIfAbsent(fixture.getLeagueId(), id -> leagueClient.findLeagueById(id).name());
                    String homeTeamName = teamNameCache.computeIfAbsent(fixture.getHomeTeamId(), id -> teamClient.findTeamById(id).name());
                    String awayTeamName = teamNameCache.computeIfAbsent(fixture.getAwayTeamId(), id -> teamClient.findTeamById(id).name());

                    return fixtureMapper.toDto(fixture, leagueName, homeTeamName, awayTeamName);
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

        return fixtureMapper.toDto(savedFixture);
    }

    public FixtureResponse update(Long fixtureId, UpdateFixtureRequest request) {
        Fixture fixture = fixtureRepository.findById(fixtureId).orElseThrow(() -> new FixtureNotFoundException("Fikstür bulunamadı !"));
        fixtureMapper.updateEntity(fixture, request);
        Fixture updatedFixture = fixtureRepository.save(fixture);

        return fixtureMapper.toDto(updatedFixture);
    }

    public void delete(Long id) {
        Fixture fixture = fixtureRepository.findById(id).orElseThrow(() -> new FixtureNotFoundException("Fikstür bulunamadı " + id));
        fixtureRepository.delete(fixture);
    }
}
