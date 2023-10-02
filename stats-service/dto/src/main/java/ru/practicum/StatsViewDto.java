package ru.practicum;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatsViewDto {
    private String app;

    private String uri;

    private long hits;
}
