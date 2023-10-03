package ru.practicum.guest.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shared.dto.EventFullDto;
import ru.practicum.shared.dto.EventShortDto;
import ru.practicum.shared.util.enums.SortEvents;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {
    EventFullDto getEvent(Long eventId, HttpServletRequest request);

    List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                  SortEvents sort, Integer from, Integer size, HttpServletRequest request);
    List<EventShortDto> getEventsInLocation(Long locationId, Float lat, Float lon,
                                                Float radius, Pageable pageable);

}
