package ru.practicum.ewmstat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmstat.model.StatHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<StatHit, Long> {
    @Query("SELECT new ru.practicum.ewmstat.StatsViewDto(s.app, s.uri, COUNT(s.ip)) " +
            "FROM StatHit AS s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<StatsViewDto> findAllStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewmstat.StatsViewDto(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM StatHit AS s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<StatsViewDto> findStatsByUniqIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.ewmstat.StatsViewDto(s.app, s.uri,  COUNT(s.ip)) " +
            "FROM StatHit AS s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "AND s.uri IN :uris " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<StatsViewDto> findStatsWithUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.ewmstat.StatsViewDto(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM StatHit AS s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "AND s.uri IN :uris " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<StatsViewDto> findStatsByUrisAndUniqIp(LocalDateTime start, LocalDateTime end, List<String> uris);

}
