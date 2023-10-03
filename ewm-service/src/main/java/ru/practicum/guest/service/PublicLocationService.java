package ru.practicum.guest.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shared.dto.LocationFullDto;

import java.util.List;


public interface PublicLocationService {
    LocationFullDto getLocation(long id);
    List<LocationFullDto> getLocations(Pageable pageable);

}
