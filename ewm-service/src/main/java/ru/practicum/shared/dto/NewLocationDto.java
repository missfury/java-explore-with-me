package ru.practicum.shared.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewLocationDto {
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @NotNull
    private Float lat;
    @NotNull
    private Float lon;
    private Float radius;

}
