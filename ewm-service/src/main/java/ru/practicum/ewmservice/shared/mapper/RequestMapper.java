package ru.practicum.ewmservice.shared.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.shared.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.shared.model.Request;

@Component
public class RequestMapper {
    public ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .status(request.getStatus())
                .build();
    }
}
