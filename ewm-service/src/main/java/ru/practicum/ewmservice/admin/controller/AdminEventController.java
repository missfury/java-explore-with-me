package ru.practicum.ewmservice.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.admin.service.AdminEventService;
import ru.practicum.ewmservice.shared.dto.EventFullDto;
import ru.practicum.ewmservice.shared.dto.UpdateEventAdminRequest;
import ru.practicum.ewmservice.shared.util.Pagination;
import ru.practicum.ewmservice.shared.util.enums.State;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
    private final AdminEventService adminEventService;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) List<State> states,
                                        @RequestParam(required = false) List<Long> categories,

                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,

                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получены события пользователей {} со статусом {}, категорий {}", users, states, categories);
        return adminEventService.getAdminEvents(users, states, categories, rangeStart, rangeEnd,
                new Pagination(from, size, Sort.unsorted()));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventAdminRequest event) {
        log.info("Обновлено событие {} с id= {}", event, eventId);
        return adminEventService.updateAdminEvent(eventId, event);
    }

}
