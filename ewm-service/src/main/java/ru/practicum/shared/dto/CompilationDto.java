package ru.practicum.shared.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private Long id;

    private Boolean pinned;

    private String title;

    private List<EventShortDto> events;
}
