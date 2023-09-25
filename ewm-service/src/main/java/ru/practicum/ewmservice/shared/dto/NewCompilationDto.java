package ru.practicum.ewmservice.shared.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    @Size(min = 1, max = 50)
    @NotBlank(message = "Title can not be blank")
    private String title;

    private Boolean pinned;

    private List<Long> events;
}
