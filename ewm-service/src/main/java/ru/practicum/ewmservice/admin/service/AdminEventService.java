package ru.practicum.ewmservice.admin.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewmservice.shared.dto.EventFullDto;
import ru.practicum.ewmservice.shared.dto.UpdateEventAdminRequest;
import ru.practicum.ewmservice.shared.util.enums.State;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {
    List<EventFullDto> getAdminEvents(List<Long> users, List<State> states, List<Long> categories,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
    EventFullDto updateAdminEvent(Long eventId, UpdateEventAdminRequest eventAdminRequest);

}
