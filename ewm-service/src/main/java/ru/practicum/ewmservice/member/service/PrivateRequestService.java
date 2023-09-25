package ru.practicum.ewmservice.member.service;

import ru.practicum.ewmservice.shared.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestService {
    ParticipationRequestDto addRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequests(Long userId);

    ParticipationRequestDto canselRequest(Long userId, Long requestId);

}
