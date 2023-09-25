package ru.practicum.ewmservice.shared.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.shared.dto.EventFullDto;
import ru.practicum.ewmservice.shared.dto.EventShortDto;
import ru.practicum.ewmservice.shared.dto.NewEventDto;
import ru.practicum.ewmservice.shared.model.Event;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class EventMapper {
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;

    public EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .confirmedRequests(event.getConfirmedRequests())
                .category(event.getCategory())
                .eventDate(event.getEventDate())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .initiator(event.getInitiator())
                .createdOn(event.getCreatedOn())
                .paid(event.getPaid())
                .state(event.getState())
                .location(event.getLocation())
                .publishedOn(event.getPublishedOn())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .build();
    }

    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public Event toEvent(NewEventDto newEventDTO) {
        return Event.builder()
                .eventDate(newEventDTO.getEventDate())
                .title(newEventDTO.getTitle())
                .annotation(newEventDTO.getAnnotation())
                .description(newEventDTO.getDescription())
                .paid(newEventDTO.getPaid())
                .requestModeration(newEventDTO.getRequestModeration())
                .participantLimit(newEventDTO.getParticipantLimit())
                .location(newEventDTO.getLocation())
                .createdOn(LocalDateTime.now())
                .build();
    }
}
