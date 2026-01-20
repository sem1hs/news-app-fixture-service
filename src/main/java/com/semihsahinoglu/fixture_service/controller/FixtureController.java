package com.semihsahinoglu.fixture_service.controller;

import com.semihsahinoglu.fixture_service.dto.CreateFixtureRequest;
import com.semihsahinoglu.fixture_service.dto.FixtureResponse;
import com.semihsahinoglu.fixture_service.dto.UpdateFixtureRequest;
import com.semihsahinoglu.fixture_service.service.FixtureService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fixture")
public class FixtureController {

    private final FixtureService fixtureService;

    public FixtureController(FixtureService fixtureService) {
        this.fixtureService = fixtureService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<FixtureResponse> getById(@PathVariable Long id) {
        FixtureResponse response = fixtureService.getById(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(params = "leagueId")
    public ResponseEntity<List<FixtureResponse>> getAllByLeagueId(@RequestParam Long leagueId) {
        List<FixtureResponse> response = fixtureService.getAllByLeagueId(leagueId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(params = {"leagueId", "week"})
    public ResponseEntity<List<FixtureResponse>> getWeeklyFixtures(@RequestParam Long leagueId, @RequestParam Integer week) {
        List<FixtureResponse> response = fixtureService.getByLeagueAndWeek(leagueId, week);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<FixtureResponse> createFixture(@Valid @RequestBody CreateFixtureRequest request) {
        FixtureResponse fixture = fixtureService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(fixture);
    }

    @PatchMapping("/{id}")
    ResponseEntity<FixtureResponse> update(@PathVariable Long id, @RequestBody UpdateFixtureRequest updateFixtureRequest) {
        FixtureResponse fixtureResponse = fixtureService.update(id, updateFixtureRequest);
        return ResponseEntity.status(HttpStatus.OK).body(fixtureResponse);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        fixtureService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
