package ru.practicum.shared.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shared.dto.ParticipationRequestDto;
import ru.practicum.shared.model.Request;

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
