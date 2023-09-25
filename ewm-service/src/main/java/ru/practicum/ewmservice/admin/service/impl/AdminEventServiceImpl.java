package ru.practicum.ewmservice.admin.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.admin.service.AdminEventService;
import ru.practicum.ewmservice.shared.dto.EventFullDto;
import ru.practicum.ewmservice.shared.dto.UpdateEventAdminRequest;
import ru.practicum.ewmservice.shared.exceptions.NotFoundException;
import ru.practicum.ewmservice.shared.exceptions.ValidateDateException;
import ru.practicum.ewmservice.shared.exceptions.ValidateException;
import ru.practicum.ewmservice.shared.mapper.EventMapper;
import ru.practicum.ewmservice.shared.model.Category;
import ru.practicum.ewmservice.shared.model.Event;
import ru.practicum.ewmservice.shared.repository.CategoryRepository;
import ru.practicum.ewmservice.shared.repository.EventRepository;
import ru.practicum.ewmservice.shared.util.enums.AdminActions;
import ru.practicum.ewmservice.shared.util.enums.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;

    @Override
    public List<EventFullDto> getAdminEvents(List<Long> users, List<State> states, List<Long> categories,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable) {
        validDateParam(rangeStart, rangeEnd);
        List<Event> events = eventRepository.findAdminEvents(users, states, categories, rangeStart, rangeEnd, pageable);
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
            event.setLocation(eventAdminRequest.getLocation());
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



}
