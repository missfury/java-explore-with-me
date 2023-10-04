package ru.practicum.shared.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShortLocationDto {
    @NotNull
    private float lat;
    @NotNull
    private float lon;
    @NotBlank
    private String address;
}
