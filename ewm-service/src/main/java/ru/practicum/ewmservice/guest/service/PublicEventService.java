package ru.practicum.ewmservice.guest.service;

import ru.practicum.ewmservice.shared.dto.EventFullDto;
import ru.practicum.ewmservice.shared.dto.EventShortDto;
import ru.practicum.ewmservice.shared.util.enums.SortEvents;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {
    EventFullDto getEvent(Long eventId, HttpServletRequest request);
    List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                  SortEvents sort, Integer from, Integer size, HttpServletRequest request);

}