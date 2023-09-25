package ru.practicum.ewmservice.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.member.service.PrivateEventService;
import ru.practicum.ewmservice.shared.dto.*;
import ru.practicum.ewmservice.shared.util.Pagination;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final PrivateEventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Создано событие {} пользователем с id= {}", newEventDto, userId);
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping
    public List<EventShortDto> getEvents(@PathVariable Long userId,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Получены события пользователя с id= {}", userId);
        return eventService.getEvents(userId, new Pagination(from, size, Sort.unsorted()));
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
        log.info("Получено событие с id= {} пользователя с id= {}", eventId, userId);
        return eventService.getEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("Событие обновлено пользователем. Id пользователя: {}, Id события: {}", userId, eventId);
        return eventService.updateEvent(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequest(@PathVariable Long userId,
                                                                 @PathVariable Long eventId) {
        log.info("Получен запрос на участие в событии с id= {} для пользователя с id{}", eventId, userId);
        return eventService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult changeStatus(@PathVariable Long userId,
                                                       @PathVariable Long eventId,
                                                       @RequestBody EventRequestStatusUpdateRequest changeRequest) {
        log.info("Обновлен запрос на участие в событии с id= {} для пользователя с id{}", eventId, userId);
        return eventService.updateStatusRequest(userId, eventId, changeRequest);
    }



}
