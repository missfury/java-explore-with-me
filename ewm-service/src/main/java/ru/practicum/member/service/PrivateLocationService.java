package ru.practicum.member.service;

import ru.practicum.shared.dto.LocationFullDto;
import ru.practicum.shared.dto.NewLocationDto;

public interface PrivateLocationService {
    LocationFullDto addLocation(NewLocationDto locationDto);
}
