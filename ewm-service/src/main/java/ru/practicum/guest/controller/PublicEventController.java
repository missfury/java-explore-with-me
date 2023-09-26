package ru.practicum.guest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.guest.service.PublicEventService;
import ru.practicum.shared.dto.EventFullDto;
import ru.practicum.shared.dto.EventShortDto;
import ru.practicum.shared.exceptions.ValidateException;
import ru.practicum.shared.util.enums.SortEvents;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/events")
public class PublicEventController {
    private final PublicEventService eventService;

    @GetMapping("/{id}")
    EventFullDto getEvent(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        log.info("Получена информация об опубликованном событии с id: {}", id);
        return eventService.getEvent(id, request);
    }

    @GetMapping
    List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                  @RequestParam(required = false) List<Long> categories,
                                  @RequestParam(required = false) Boolean paid,
                                  @RequestParam(required = false)
                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                  @RequestParam(required = false)
                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                  @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                  @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                  @RequestParam(defaultValue = "0")
                                  @PositiveOrZero Integer from,
                                  @RequestParam(defaultValue = "10")
                                  @Positive Integer size,
                                  HttpServletRequest request) {
        SortEvents sortParam = SortEvents.from(sort).orElseThrow(() -> new ValidateException("Sort isn't valid: "
                + sort));
        log.info("Получен список опубликованных событий");
        return eventService.getEvents(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sortParam, from, size, request);
    }

}
