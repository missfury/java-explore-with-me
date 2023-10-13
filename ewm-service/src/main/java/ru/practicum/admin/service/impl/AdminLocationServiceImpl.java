package ru.practicum.admin.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.admin.service.AdminLocationService;
import ru.practicum.shared.dto.LocationFullDto;
import ru.practicum.shared.dto.NewLocationDto;
import ru.practicum.shared.dto.UpdateLocationDto;
import ru.practicum.shared.exceptions.NotFoundException;
import ru.practicum.shared.exceptions.ValidateException;
import ru.practicum.shared.mapper.LocationMapper;
import ru.practicum.shared.model.Location;
import ru.practicum.shared.repository.LocationRepository;
import org.springframework.data.domain.Pageable;
import ru.practicum.shared.util.enums.LocationStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class AdminLocationServiceImpl implements AdminLocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public LocationFullDto addLocation(NewLocationDto locationDto) {
        Location existingLocationByName = locationRepository.findByNameIgnoreCase(locationDto.getName());
        if (existingLocationByName != null) {
            throw new ValidateException("Локация с таким именем уже существует");
        }
        Location existingLocationByCoordinates = locationRepository.findByLatAndLon(
                locationDto.getLat(), locationDto.getLon());
        if (existingLocationByCoordinates != null) {
            throw new ValidateException("Локация с такими координатами уже существует");
        }
        Location location = locationMapper.toLocation(locationDto);
        location.setStatus(LocationStatus.APPROVED);
        Location savedLocation = locationRepository.save(location);
        return locationMapper.toNewLocationDto(savedLocation);
    }

    @Override
    public LocationFullDto updateLocation(Long id, UpdateLocationDto updateLocationDto) {
        Location location = getLocationById(id);
        if (updateLocationDto.getLat() != null) {
            location.setLat(updateLocationDto.getLat());
        }
        if (updateLocationDto.getLon() != null) {
            location.setLon(updateLocationDto.getLon());
        }
        if (updateLocationDto.getName() != null) {
            location.setName(updateLocationDto.getName());
        }
        if (updateLocationDto.getAddress() != null) {
            location.setAddress(updateLocationDto.getAddress());
        }
        if (updateLocationDto.getStatus() != null) {
            location.setStatus(updateLocationDto.getStatus());
        }
        locationRepository.save(location);
        return locationMapper.toNewLocationDto(location);
    }

    @Override
    public void deleteLocation(Long id) {
        Location locationToDelete = getLocationById(id);
        locationRepository.delete(locationToDelete);
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Локация не найдена"));
    }

    @Override
    public List<LocationFullDto> getAllLocations(Pageable pageable) {
        return locationRepository.findAll(pageable).stream()
                .map(locationMapper::toLocationFullDto)
                .collect(Collectors.toList());
    }

}
