package ru.practicum.ewmservice.shared.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {
    private Long id;

    private String name;
}
