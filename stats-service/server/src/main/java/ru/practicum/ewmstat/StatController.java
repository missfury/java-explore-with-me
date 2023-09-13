package ru.practicum.ewmstat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatController {
    private final StatService service;

    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void saveStatsHit(@RequestBody @Valid StatsHitDto statsHitDto) {
        log.info("Статистика сохранена {}", statsHitDto);
        service.saveStat(statsHitDto);
    }

    @GetMapping("/stats")
    public Collection<StatsViewDto> getViewStats(
            @RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam(value = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(value = "uris", defaultValue = "") List<String> uris,
            @RequestParam(value = "unique", defaultValue = "false") Boolean unique
    ) {
        log.info("Получена статистика просмотров с временем начала {} временем окончания {}," +
                " uris {} статусом уникальности ip {}", start, end, uris, unique);
        return service.getStats(start, end, uris, unique);
    }
}
