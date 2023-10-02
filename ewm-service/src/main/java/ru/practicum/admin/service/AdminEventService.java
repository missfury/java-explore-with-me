package ru.practicum.admin.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shared.dto.EventFullDto;
import ru.practicum.shared.dto.UpdateEventAdminRequest;
import ru.practicum.shared.util.enums.State;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {
    List<EventFullDto> getAdminEvents(List<Long> users, List<State> states, List<Long> categories,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    EventFullDto updateAdminEvent(Long eventId, UpdateEventAdminRequest eventAdminRequest);

}
