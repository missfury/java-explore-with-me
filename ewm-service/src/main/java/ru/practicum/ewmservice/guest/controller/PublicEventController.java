package ru.practicum.ewmservice.guest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.guest.service.PublicEventService;
import ru.practicum.ewmservice.shared.dto.EventFullDto;
import ru.practicum.ewmservice.shared.dto.EventShortDto;
import ru.practicum.ewmservice.shared.util.enums.SortEvents;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/events")
public class PublicEventController {
    private final PublicEventService eventService;

    @GetMapping("/{id}")
    EventFullDto getEvent(HttpServletRequest request, @PathVariable Long id) {
        log.info("Получена информация об опубликованном событии с id: {}", id);
        return eventService.getEvent(id, request);
    }

    @GetMapping
    List<EventShortDto> getEvents(HttpServletRequest request,
                                  @RequestParam(required = false) String text,
                                  @RequestParam(required = false) List<Long> categories,
                                  @RequestParam(required = false) Boolean paid,
                                  @RequestParam(required = false)
                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                  @RequestParam(required = false)
                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                  @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                  @RequestParam(required = false) SortEvents sort,
                                  @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("Получен список опубликованных событий");
        return eventService.getEvents(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

}
