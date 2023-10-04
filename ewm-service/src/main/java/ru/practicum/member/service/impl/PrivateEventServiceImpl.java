package ru.practicum.member.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.member.service.PrivateEventService;
import ru.practicum.shared.exceptions.NotFoundException;
import ru.practicum.shared.exceptions.ValidateDateException;
import ru.practicum.shared.exceptions.ValidateException;
import ru.practicum.shared.mapper.EventMapper;
import ru.practicum.shared.mapper.RequestMapper;
import ru.practicum.shared.model.*;
import ru.practicum.shared.repository.*;
import ru.practicum.shared.util.enums.LocationStatus;
import ru.practicum.shared.util.enums.RequestStatus;
import ru.practicum.shared.util.enums.State;
import ru.practicum.shared.util.enums.UserActions;
import ru.practicum.shared.dto.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final EventMapper eventMapper;
    private final RequestMapper requestsMapper;

    @Override
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        User user = getUser(userId);
        Category category = getCategory(newEventDto.getCategory());
        Location location = getLocation(newEventDto.getLocation());
        checkEventDate(newEventDto.getEventDate());

        Event event = eventMapper.toEvent(newEventDto, location);
        event.setInitiator(user);
        event.setCategory(category);
        event.setState(State.PENDING);

        EventFullDto eventFullDto = eventMapper.toEventFullDto(eventRepository.save(event));
        eventFullDto.setViews(0L);

        return eventFullDto;
    }

    @Override
    public EventFullDto getEventById(Long userId, Long eventId) {
        Event event = getEventInitiator(eventId, userId);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getEvents(Long userId, Pageable pageable) {
        getUser(userId);
        List<Event> events = eventRepository.findEventsByInitiatorId(userId, pageable);

        return events.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId,
                                    UpdateEventUserRequest updateEventUserRequest) {
        Event event = getEventInitiator(eventId, userId);
        checkEventDate(updateEventUserRequest.getEventDate());

        if (event.getState().equals(State.PUBLISHED)) {
            throw new ValidateException("Невозможно изменить уже опубликованное событие");
        }

        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction() == UserActions.SEND_TO_REVIEW) {
                event.setState(State.PENDING);
            } else if (updateEventUserRequest.getStateAction() == UserActions.CANCEL_REVIEW) {
                event.setState(State.CANCELED);
            }
        }

        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }

        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }

        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
        }

        if (updateEventUserRequest.getCategory() != null) {
            Category category = getCategory(updateEventUserRequest.getCategory());
            event.setCategory(category);
        }

        if (updateEventUserRequest.getEventDate() != null) {
            event.setEventDate(updateEventUserRequest.getEventDate());
        }

        if (updateEventUserRequest.getLocation() != null) {
            Location location = getLocation(updateEventUserRequest.getLocation());
            event.setLocation(location);
        }

        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }

        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }

        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }

        return eventMapper.toEventFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(Long userId, Long eventId) {
        getEventInitiator(eventId, userId);
        return requestRepository.findRequestsByEventId(eventId).stream()
                .map(requestsMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateStatusRequest(Long userId, Long eventId,
                                                              EventRequestStatusUpdateRequest eventRequest) {
        Event event = getEventInitiator(eventId, userId);
        checkModerationAndParticipantLimit(event.getRequestModeration(), event.getParticipantLimit());

        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult(
                new ArrayList<>(), new ArrayList<>());

        Integer confirmedRequests = requestRepository
                .findByEventIdConfirmed(eventId).size();

        List<Request> requests = requestRepository
                .findByEventIdAndRequestsIds(eventId, eventRequest.getRequestIds());

        if (eventRequest.getStatus().equals(RequestStatus.CONFIRMED)) {
            if (eventRequest.getRequestIds().size() <= (event.getParticipantLimit() - confirmedRequests)) {
                requests.forEach(request -> request.setStatus(RequestStatus.CONFIRMED));

                List<ParticipationRequestDto> requestDto = requests.stream()
                        .map(requestsMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());

                result.setConfirmedRequests(requestDto);

            } else if ((confirmedRequests + requests.size()) > event.getParticipantLimit()) {
                requests.forEach(request -> request.setStatus(RequestStatus.REJECTED));

                List<ParticipationRequestDto> requestDTO = requests.stream()
                        .map(requestsMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());

                result.setRejectedRequests(requestDTO);

                throw new ValidateException("Превышем лимит запросов");
            }

        } else if (eventRequest.getStatus().equals(RequestStatus.REJECTED)) {
            for (Request request : requests) {
                if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
                    throw new ValidateException("Вы не можете отклонить подтвержденный запрос");
                }

                request.setStatus(RequestStatus.REJECTED);
            }

            List<ParticipationRequestDto> requestDto = requests.stream()
                    .map(requestsMapper::toParticipationRequestDto)
                    .collect(Collectors.toList());

            result.setRejectedRequests(requestDto);
        }

        return result;
    }


    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Не удалось найти пользователя с id=%s", userId)));
    }

    private Category getCategory(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Не удалось найти категорию с id=%s", categoryId)));
    }

    private Event getEventInitiator(long eventId, long userId) {
        return eventRepository.findEventByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed to find event with id=%s, user id=%s", eventId, userId)));
    }

    private Location getLocation(ShortLocationDto locationDto) {
        Location existingLocation = locationRepository.findByLatAndLon(locationDto.getLat(), locationDto.getLon());

        if (existingLocation != null) {
            return existingLocation;
        } else {
            Location newLocation = new Location();
            newLocation.setLat(locationDto.getLat());
            newLocation.setLon(locationDto.getLon());
            newLocation.setName(newLocation.getName());
            newLocation.setName(newLocation.getAddress());
            newLocation.setName(String.valueOf(newLocation.getRadius()));
            newLocation.setStatus(LocationStatus.SUGGESTED);

            return locationRepository.save(newLocation);
        }
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidateDateException(
                    String.format("Время начала события должно быть позже текущего времени: %s", eventDate));
        }
    }

    private void checkModerationAndParticipantLimit(boolean moderation, long limit) {
        if (!moderation || limit == 0) {
            throw new ValidateException("Подтверждение не требуется");
        }
    }
}
