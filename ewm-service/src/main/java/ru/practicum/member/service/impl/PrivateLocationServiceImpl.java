package ru.practicum.member.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.member.service.PrivateLocationService;
import ru.practicum.shared.dto.LocationFullDto;
import ru.practicum.shared.dto.NewLocationDto;
import ru.practicum.shared.exceptions.ValidateException;
import ru.practicum.shared.mapper.LocationMapper;
import ru.practicum.shared.model.Location;
import ru.practicum.shared.repository.LocationRepository;
import ru.practicum.shared.util.enums.LocationStatus;


@Service
@Transactional
@AllArgsConstructor
public class PrivateLocationServiceImpl implements PrivateLocationService {
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
        location.setStatus(LocationStatus.SUGGESTED);
        Location savedLocation = locationRepository.save(location);
        return locationMapper.toNewLocationDto(savedLocation);
    }
}
