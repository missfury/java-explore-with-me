package ru.practicum.member.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.member.service.PrivateLocationService;
import ru.practicum.shared.dto.LocationFullDto;
import ru.practicum.shared.dto.NewLocationDto;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/locations")
public class PrivateLocationController {
    private final PrivateLocationService privateLocationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationFullDto addLocation(@PathVariable Long userId, @Valid @RequestBody NewLocationDto newLocationDto) {
        LocationFullDto location = privateLocationService.addLocation(newLocationDto);
        log.info("Предложена новая локация {} пользователем с id= {}", newLocationDto, userId);
        return location;
    }
}
