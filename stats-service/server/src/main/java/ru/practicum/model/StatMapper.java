package ru.practicum.model;

import ru.practicum.StatsHitDto;

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
