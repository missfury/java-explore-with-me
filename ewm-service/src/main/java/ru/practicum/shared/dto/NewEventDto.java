package ru.practicum.shared.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import ru.practicum.shared.model.Location;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull(message = "Category cannot be null")
    private Long category;

    @NotNull(message = "EventDate cannot be null")
    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull(message = "Location cannot be null")
    @Valid
    private Location location;

    @NotNull(message = "Paid cannot be null")
    @Value("false")
    private Boolean paid = false;

    @Value("0")
    @PositiveOrZero
    private Long participantLimit = 0L;

    @NotNull(message = "RequestModeration cannot be null")
    @Value("true")
    private Boolean requestModeration = true;
}
