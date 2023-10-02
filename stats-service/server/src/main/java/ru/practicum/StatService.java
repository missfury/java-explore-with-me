package ru.practicum;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatService {
    void saveStat(StatsHitDto statsHitDto);

    Collection<StatsViewDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
