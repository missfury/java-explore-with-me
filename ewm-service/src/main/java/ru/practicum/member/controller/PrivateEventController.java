package ru.practicum.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.member.service.PrivateEventService;
import ru.practicum.shared.dto.*;
import ru.practicum.shared.util.Pagination;


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
        EventFullDto event = eventService.addEvent(userId, newEventDto);
        log.info("Создано событие {} пользователем с id= {}", newEventDto, userId);
        return event;
    }

    @GetMapping
    public List<EventShortDto> getEvents(@PathVariable Long userId,
                                         @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size) {
        List<EventShortDto> events = eventService.getEvents(userId, new Pagination(from, size, Sort.unsorted()));
        log.info("Получены события пользователя с id= {}", userId);
        return events;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
        EventFullDto event = eventService.getEventById(userId, eventId);
        log.info("Получено событие с id= {} пользователя с id= {}", eventId, userId);
        return event;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        EventFullDto event = eventService.updateEvent(userId, eventId, updateEventUserRequest);
        log.info("Событие обновлено пользователем. Id пользователя: {}, Id события: {}", userId, eventId);
        return event;
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequest(@PathVariable Long userId,
                                                                 @PathVariable Long eventId) {
        List<ParticipationRequestDto> request = eventService.getEventRequests(userId, eventId);
        log.info("Получен запрос на участие в событии с id= {} для пользователя с id{}", eventId, userId);
        return request;
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult changeStatus(@PathVariable Long userId,
                                                       @PathVariable Long eventId,
                                                       @RequestBody EventRequestStatusUpdateRequest changeRequest) {
        EventRequestStatusUpdateResult status = eventService.updateStatusRequest(userId, eventId, changeRequest);
        log.info("Обновлен запрос на участие в событии с id= {} для пользователя с id{}", eventId, userId);
        return status;
    }



}
