package ru.practicum.admin.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shared.dto.LocationFullDto;
import ru.practicum.shared.dto.NewLocationDto;
import ru.practicum.shared.dto.UpdateLocationDto;

import java.util.List;

public interface AdminLocationService {
    LocationFullDto addLocation(NewLocationDto locationDTO);
    LocationFullDto updateLocation(Long id, UpdateLocationDto updateLocationDto);
    void deleteLocation(Long id);
    List<LocationFullDto> getAllLocations(Pageable pageable);
}
