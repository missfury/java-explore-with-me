package ru.practicum.shared.dto;

import lombok.*;
import ru.practicum.shared.util.enums.LocationStatus;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLocationDto {
    private Float lat;
    private Float lon;
    @Size(max = 250)
    private String name;
    @Size(max = 1000)
    private String address;
    private LocationStatus status;
}
