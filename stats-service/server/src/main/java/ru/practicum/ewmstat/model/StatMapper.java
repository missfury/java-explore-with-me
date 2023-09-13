package ru.practicum.ewmstat.model;

import ru.practicum.ewmstat.StatsHitDto;

public class StatMapper {
    public static StatHit statsHitDtoToStatHit(StatsHitDto statsHitDto) {
        return StatHit.builder()
                .app(statsHitDto.getApp())
                .uri(statsHitDto.getUri())
                .ip(statsHitDto.getIp())
                .timestamp(statsHitDto.getTimestamp())
                .build();
    }
}
