package com.semihsahinoglu.fixture_service.repository;

import com.semihsahinoglu.fixture_service.entity.Fixture;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FixtureRepository extends JpaRepository<Fixture, Long> {

    Optional<List<Fixture>> findByLeagueIdOrderByMatchDateAsc(Long leagueId);

    Optional<List<Fixture>> findByLeagueIdAndWeekOrderByMatchDateAsc(Long leagueId, Integer week);

    @Query("SELECT COUNT(f) > 0 FROM Fixture f WHERE f.leagueId = :leagueId AND f.week = :week AND (f.homeTeamId = :teamId OR f.awayTeamId = :teamId)")
    boolean existsTeamInWeek(@Param("leagueId") Long leagueId, @Param("week") Integer week, @Param("teamId") Long teamId);

    @Query("SELECT f FROM Fixture f WHERE f.matchDate >= :startOfDay AND f.matchDate < :endOfDay ORDER BY f.matchDate ASC")
    List<Fixture> findTodayFixtures(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

}
