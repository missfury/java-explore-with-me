package ru.practicum.ewmservice.member.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewmservice.shared.dto.*;

import java.util.List;

public interface PrivateEventService {
    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEventById(Long userId, Long eventId);

    List<EventShortDto> getEvents(Long userId, Pageable pageable);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId);

    public EventRequestStatusUpdateResult updateStatusRequest(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest eventRequest);

}
