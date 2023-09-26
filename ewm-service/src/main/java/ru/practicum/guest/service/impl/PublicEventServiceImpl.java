package ru.practicum.ewmservice.guest.service.impl;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.guest.service.PublicEventService;
import ru.practicum.ewmservice.shared.dto.EventFullDto;
import ru.practicum.ewmservice.shared.dto.EventShortDto;
import ru.practicum.ewmservice.shared.exceptions.NotFoundException;
import ru.practicum.ewmservice.shared.exceptions.ValidateDateException;
import ru.practicum.ewmservice.shared.mapper.EventMapper;
import ru.practicum.ewmservice.shared.model.Event;
import ru.practicum.ewmservice.shared.model.Request;
import ru.practicum.ewmservice.shared.repository.EventRepository;
import ru.practicum.ewmservice.shared.repository.RequestRepository;
import ru.practicum.ewmservice.shared.util.Pagination;
import ru.practicum.ewmservice.shared.util.enums.RequestStatus;
import ru.practicum.ewmservice.shared.util.enums.SortEvents;
import ru.practicum.ewmservice.shared.util.enums.State;
import ru.practicum.ewmstat.StatsClient;
import ru.practicum.ewmstat.StatsViewDto;
import ru.practicum.ewmstat.model.StatMapper;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.ewmservice.shared.util.enums.SortEvents.EVENT_DATE;
import static ru.practicum.ewmservice.shared.util.enums.SortEvents.VIEWS;
import static ru.practicum.ewmservice.shared.util.enums.State.PUBLISHED;

@Service
@Transactional
@RequiredArgsConstructor
@PropertySource(value = {"classpath:application.properties"})
public class PublicEventServiceImpl implements PublicEventService {
    @Value(value = "${app.name}")
    private String app;
    public static final String START_DATE = LocalDateTime.now().minusDays(1000)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    public static final String END_DATE = LocalDateTime.now().plusDays(1000)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    public static final DateTimeFormatter START_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;
    //private final StatMapper statMapper;

    @Override
    public EventFullDto getEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие с id=" + eventId + " не найдено"));
        confirmedRequestsForOneEvent(event);
        if (!event.getState().equals(PUBLISHED)) {
            throw new NotFoundException("Событие с id=" + eventId + " не опубликовано");
        }
        EventFullDto fullDto = eventMapper.toEventFullDto(event);

        /*String[] uris;
        uris = List.of("/events/" + event.getId()).toArray(new String[0]);
        List<StatsViewDto> views = statsClient.callEndpointStats(START_DATE, END_DATE, uris, null).getBody();

        if (views != null) {
            fullDto.setViews(Long.valueOf(views.size()));
        }
        statsClient.callEndpointHit(app, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());*/

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
        final State state = PUBLISHED;
        List<Event> events;

        if (sort.equals(EVENT_DATE)) {
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

        if (rangeEnd != null) {
            events = getEventsBeforeRangeEnd(events, rangeEnd);
        }

        confirmedRequestForListEvent(events);

        List<EventShortDto> result = events.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());

        //statsClient.callEndpointHit(app, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        if (sort.equals(VIEWS)) {
            return result.stream()
                    .sorted(Comparator.comparingLong(EventShortDto::getViews))
                    .collect(Collectors.toList());
        }

        return result;
    }

    private Pageable pageableWithSort(int from, int size, SortEvents sort) {
        if (sort == VIEWS) {
            return new Pagination(from, size, Sort.by("views"));
        } else if (sort == EVENT_DATE) {
            return new Pagination(from, size, Sort.by("eventDate"));
        } else {
            return new Pagination(from, size, Sort.unsorted());
        }
    }

    private Long getId(String url) {
        String[] uri = url.split("/");
        return Long.valueOf(uri[uri.length - 1]);
    }

    /*private void saveViewInEvent(List<EventShortDto> result, LocalDateTime rangeStart) {
        String[] uris;
        uris = result.stream()
                .map(eventShortDto -> "/events/" + eventShortDto.getId())
                .collect(Collectors.toList()).toArray(new String[0]);

        if (rangeStart != null) {
            List<StatsViewDto> views = statsClient.callEndpointStats(
                    rangeStart.format(START_DATE_FORMATTER), LocalDateTime.now().format(START_DATE_FORMATTER),
                    uris, true).getBody();

            if (views != null) {
                Map<Long, Long> mapIdHits = views.stream()
                        .collect(Collectors.toMap(viewStats -> getId(viewStats.getUri()), StatsViewDto::getHits));

                result.forEach(eventShortDto -> {
                    Long eventId = eventShortDto.getId();
                    Long viewsCount = mapIdHits.getOrDefault(eventId, 0L);
                    eventShortDto.setViews(viewsCount);
                });
            }
        }
    } */


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

    private LocalDateTime getRangeStart(LocalDateTime rangeStart) {
        if (rangeStart == null) {
            return LocalDateTime.now();
        }
        return rangeStart;
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

    private Long getConfirmedRequestsByEvent(Event event) {
        return (long) requestRepository.findByEventAndStatus(event, RequestStatus.CONFIRMED).size();
    }


    public StatsClient getStatsClient() {
        return statsClient;
    }
}
