package ru.practicum.admin.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.service.AdminEventService;
import ru.practicum.shared.dto.EventFullDto;
import ru.practicum.shared.dto.ShortLocationDto;
import ru.practicum.shared.dto.UpdateEventAdminRequest;
import ru.practicum.shared.exceptions.NotFoundException;
import ru.practicum.shared.exceptions.ValidateDateException;
import ru.practicum.shared.exceptions.ValidateException;
import ru.practicum.shared.mapper.EventMapper;
import ru.practicum.shared.model.Category;
import ru.practicum.shared.model.Event;
import ru.practicum.shared.model.Location;
import ru.practicum.shared.repository.CategoryRepository;
import ru.practicum.shared.repository.EventRepository;
import ru.practicum.shared.repository.LocationRepository;
import ru.practicum.shared.repository.RequestRepository;
import ru.practicum.shared.util.enums.AdminActions;
import ru.practicum.shared.util.enums.RequestStatus;
import ru.practicum.shared.util.enums.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final EventMapper eventMapper;

    @Override
    public List<EventFullDto> getAdminEvents(List<Long> users, List<State> states, List<Long> categories,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable) {
        validDateParam(rangeStart, rangeEnd);
        List<Event> events = eventRepository.findEventsByFilters(users, states, categories, rangeStart, rangeEnd,
                pageable);
        for (Event event : events) {
            event.setConfirmedRequests(getConfirmedRequestsByEvent(event));
        }
        return events.stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateAdminEvent(Long eventId, UpdateEventAdminRequest eventAdminRequest) {
        Event event = getEvent(eventId);

        if (eventAdminRequest.getTitle() != null) {
            event.setTitle(eventAdminRequest.getTitle());
        }

        if (eventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(eventAdminRequest.getAnnotation());
        }

        if (eventAdminRequest.getCategory() != null) {
            Category category = getCategory(eventAdminRequest.getCategory());
            event.setCategory(category);
        }

        if (eventAdminRequest.getDescription() != null) {
            event.setDescription(eventAdminRequest.getDescription());
        }

        if (eventAdminRequest.getEventDate() != null) {
            if (eventAdminRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new ValidateDateException("Время начала события не может быть раньше, " +
                        "чем через 1 час после времени публикации");
            } else {
                event.setEventDate(eventAdminRequest.getEventDate());
            }
        }

        if (eventAdminRequest.getLocation() != null) {
            Location location = getLocation(eventAdminRequest.getLocation());
            event.setLocation(location);
        }

        if (eventAdminRequest.getPaid() != null) {
            event.setPaid(eventAdminRequest.getPaid());
        }

        if (eventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(eventAdminRequest.getParticipantLimit());
        }

        if (eventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(eventAdminRequest.getRequestModeration());
        }

        if (eventAdminRequest.getStateAction() != null) {
            if (!event.getState().equals(State.PENDING)) {
                throw new ValidateException(
                        String.format("Невозможно разместить событие из-за некорретного статуса: %s",
                                event.getState()));
            }

            if (eventAdminRequest.getStateAction() == AdminActions.PUBLISH_EVENT) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (eventAdminRequest.getStateAction() == AdminActions.REJECT_EVENT) {
                event.setState(State.REJECTED);
            }
        }

        return eventMapper.toEventFullDto(event);
    }

    private void validDateParam(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new ValidateDateException("Время окончания не может быть раньше времени начала");
            }
        }
    }

    private Event getEvent(long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Не удалось найти событие с id=%d", eventId)));
    }

    private Category getCategory(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Не удалось найти категорию с id=%d", categoryId)));
    }

    private Location getLocation(ShortLocationDto locationDto) {
        Location existingLocation = locationRepository.findByLatAndLon(locationDto.getLat(), locationDto.getLon());

        if (existingLocation != null) {
            return existingLocation;
        } else {
            Location newLocation = new Location();
            newLocation.setLat(locationDto.getLat());
            newLocation.setLon(locationDto.getLon());

            return locationRepository.save(newLocation);
        }
    }


    private Long getConfirmedRequestsByEvent(Event event) {
        return (long) requestRepository.findByEventAndStatus(event, RequestStatus.CONFIRMED).size();
    }



}
