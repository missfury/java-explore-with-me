package ru.practicum.ewmservice.member.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.member.service.PrivateRequestService;
import ru.practicum.ewmservice.shared.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.shared.exceptions.NotFoundException;
import ru.practicum.ewmservice.shared.exceptions.ValidateException;
import ru.practicum.ewmservice.shared.mapper.RequestMapper;
import ru.practicum.ewmservice.shared.model.Event;
import ru.practicum.ewmservice.shared.model.Request;
import ru.practicum.ewmservice.shared.model.User;
import ru.practicum.ewmservice.shared.repository.EventRepository;
import ru.practicum.ewmservice.shared.repository.RequestRepository;
import ru.practicum.ewmservice.shared.repository.UserRepository;
import ru.practicum.ewmservice.shared.util.enums.RequestStatus;
import ru.practicum.ewmservice.shared.util.enums.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class PrivateRequestServiceImpl implements PrivateRequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestMapper requestsMapper;

    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
        Event event = eventRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Событие с id=" + eventId + "  не найдено"));

        RequestStatus status;

        int confirmedRequests = requestRepository
                .findByEventIdAndStatus(eventId, RequestStatus.CONFIRMED).size();

        requestRepository.findByEventAndRequesterId(eventId, userId).ifPresent(request -> {
            throw new ValidateException("Вы не можете повторно создать уже существующий запрос");
        });

        if (userId.equals(event.getInitiator().getId())) {
            throw new ValidateException("Организатор события не может быть его участником");
        }

        if (event.getState() != State.PUBLISHED) {
            throw new ValidateException("Нельзя присоединиться к событию, т.к. оно еще не было опубликовано");
        }

        if ((event.getParticipantLimit() != 0) && (event.getParticipantLimit() <= confirmedRequests)) {
            throw new ValidateException("Достигнут лимит участников");
        }

        if (!event.getRequestModeration() || (event.getParticipantLimit() == 0)) {
            status = RequestStatus.CONFIRMED;
        } else {
            status = RequestStatus.PENDING;
        }

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(status)
                .build();

        return requestsMapper.toParticipationRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + userId + " не найден"));
        return requestRepository.findByRequesterId(userId).stream()
                .map(requestsMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto canselRequest(Long userId, Long requestId) {
        Request request = requestRepository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Запрос с id=" + requestId + " не найден"));
        request.setStatus(RequestStatus.CANCELED);
        return requestsMapper.toParticipationRequestDto(request);
    }

}
