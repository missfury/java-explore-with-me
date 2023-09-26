package ru.practicum.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.member.service.PrivateRequestService;
import ru.practicum.shared.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class PrivateRequestController {
    private final PrivateRequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ParticipationRequestDto addRequest(@PathVariable Long userId,
                                       @RequestParam Long eventId) {
        log.info("Создан запрос на участие в событии с id= {} для пользователя с id= {} ", eventId, userId);
        return requestService.addRequest(userId, eventId);
    }

    @GetMapping
    List<ParticipationRequestDto> getRequests(@PathVariable Long userId) {
        log.info("Получены запросы для пользователя с id: {}", userId);
        return requestService.getRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    ParticipationRequestDto canselRequest(@PathVariable Long userId,
                                          @PathVariable Long requestId) {
        log.info("Пользователем с id= {} отменен запрос на участие в событии с id= {} ", userId, requestId);
        return requestService.canselRequest(userId, requestId);
    }
}
