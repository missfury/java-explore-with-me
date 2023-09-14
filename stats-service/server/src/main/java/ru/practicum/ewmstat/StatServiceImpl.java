package ru.practicum.ewmstat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmstat.exceptions.ValidationException;
import ru.practicum.ewmstat.model.StatHit;
import ru.practicum.ewmstat.model.StatMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StatServiceImpl implements StatService {
    private final StatRepository repository;

    @Override
    public void saveStat(StatsHitDto dto) {
        StatHit statHit = repository.save(StatMapper.statsHitDtoToStatHit(dto));
        log.info("Статистика сохранена {}", statHit);
    }

    @Override
    public List<StatsViewDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end)) {
            log.info("Время окончания не может быть раньше времени начала");
            throw new ValidationException("Время окончания не может быть раньше времени начала");
        }

        return (CollectionUtils.isEmpty(uris)) ?
                (unique ? repository.findStatsByUniqIp(start, end) : repository.findAllStats(start, end)) :
                (unique ? repository.findStatsByUrisAndUniqIp(start, end, uris) :
                        repository.findStatsWithUris(start, end, uris));
    }
}
