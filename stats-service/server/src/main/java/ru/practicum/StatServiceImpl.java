package ru.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ValidationException;
import ru.practicum.model.StatHit;
import ru.practicum.model.StatMapper;
import org.springframework.transaction.annotation.Transactional;

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

        if (uris.isEmpty()) {
            if (unique) {
                return repository.findStatsByUniqIp(start, end);
            } else {
                return repository.findAllStats(start, end);
            }
        } else {
            if (unique) {
                return repository.findStatsByUrisAndUniqIp(start, end, uris);
            } else {
                return repository.findStatsWithUris(start, end, uris);
            }
        }
    }
}
