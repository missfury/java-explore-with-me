package ru.practicum.shared.dto;

import lombok.*;
import ru.practicum.shared.util.enums.LocationStatus;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationFullDto {
    private long id;
    private Float lat;
    private Float lon;
    private String name;
    private String address;
    private Float radius;
    private LocationStatus status;
}
