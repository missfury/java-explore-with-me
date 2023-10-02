package ru.practicum.guest.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsViewDto;
import ru.practicum.guest.service.PublicEventService;
import ru.practicum.shared.dto.EventFullDto;
import ru.practicum.shared.dto.EventShortDto;
import ru.practicum.shared.exceptions.NotFoundException;
import ru.practicum.shared.exceptions.ValidateDateException;
import ru.practicum.shared.mapper.EventMapper;
import ru.practicum.shared.model.Event;
import ru.practicum.shared.model.Request;
import ru.practicum.shared.repository.EventRepository;
import ru.practicum.shared.repository.RequestRepository;
import ru.practicum.shared.util.Pagination;
import ru.practicum.shared.util.enums.RequestStatus;
import ru.practicum.shared.util.enums.SortEvents;
import ru.practicum.shared.util.enums.State;
import ru.practicum.StatsClient;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    @Value(value = "${app.name}")
    private String app;
    public static final String START_DATE = LocalDateTime.now().minusDays(1000)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    public static final String END_DATE = LocalDateTime.now().plusDays(1000)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;


    @Override
    public EventFullDto getEvent(Long eventId, HttpServletRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));
        confirmedRequestsForOneEvent(event);
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Событие с id=" + eventId + " не опубликовано");
        }
        EventFullDto fullDto = eventMapper.toEventFullDto(event);

        String[] uris;
        uris = List.of("/events/" + event.getId()).toArray(new String[0]);
        List<StatsViewDto> views = statsClient.callEndpointStats(START_DATE, END_DATE, uris, null).getBody();

        if (views != null) {
            fullDto.setViews(Long.valueOf(views.size()));
        }
        statsClient.callEndpointHit(app, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        return fullDto;
    }

    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Boolean onlyAvailable, SortEvents sort, Integer from,
                                         Integer size, HttpServletRequest request) {

        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }

        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.MAX;
        }

        validDateParam(rangeStart, rangeEnd);
        Pagination pageable;
        final State state = State.PUBLISHED;
        List<Event> events;

        if (sort.equals(SortEvents.EVENT_DATE)) {
            pageable = new Pagination(from, size, Sort.by("eventDate"));
        } else {
            pageable = new Pagination(from, size, Sort.unsorted());
        }

        if (onlyAvailable) {
            events = eventRepository.findAllPublishStateNotAvailable(state, rangeStart, categories,
                    paid, text, pageable);
        } else {
            events = eventRepository.findAllPublishStateAvailable(state, rangeStart, categories,
                    paid, text, pageable);
        }

        events = getEventsBeforeRangeEnd(events, rangeEnd);

        confirmedRequestForListEvent(events);

        List<EventShortDto> result = events.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());

        statsClient.callEndpointHit(app, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        if (sort.equals(SortEvents.VIEWS)) {
            return result.stream()
                    .sorted(Comparator.comparingLong(EventShortDto::getViews))
                    .collect(Collectors.toList());
        }

        return result;
    }

    public void confirmedRequestsForOneEvent(Event event) {
        event.setConfirmedRequests(requestRepository
                .countRequestByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED));
    }

    private void validDateParam(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new ValidateDateException("Время окончания не может быть раньше времени начала");
            }
        }
    }

    private List<Event> getEventsBeforeRangeEnd(List<Event> events, LocalDateTime rangeEnd) {
        return events.stream().filter(event -> event.getEventDate().isBefore(rangeEnd)).collect(Collectors.toList());
    }

    public void confirmedRequestForListEvent(List<Event> events) {
        Map<Event, Long> requestsPerEvent = requestRepository.findAllByEventInAndStatus(events, RequestStatus.CONFIRMED)
                .stream()
                .collect(Collectors.groupingBy(Request::getEvent, Collectors.counting()));
        if (!requestsPerEvent.isEmpty()) {
            for (Event event : events) {
                event.setConfirmedRequests(requestsPerEvent.getOrDefault(event, 0L));
            }
        }
    }

}
