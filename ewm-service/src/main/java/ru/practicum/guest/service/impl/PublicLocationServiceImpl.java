package ru.practicum.guest.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.guest.service.PublicLocationService;
import ru.practicum.shared.dto.LocationFullDto;
import ru.practicum.shared.exceptions.NotAvailableException;
import ru.practicum.shared.exceptions.NotFoundException;
import ru.practicum.shared.mapper.LocationMapper;
import ru.practicum.shared.model.Location;
import ru.practicum.shared.repository.LocationRepository;
import ru.practicum.shared.util.enums.LocationStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PublicLocationServiceImpl implements PublicLocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public LocationFullDto getLocation(long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Локация не найдена"));
        if (location.getStatus() != LocationStatus.APPROVED) {
            throw new NotAvailableException("Локация недоступна");
        }
        return locationMapper.toLocationFullDto(location);
    }

    @Override
    public List<LocationFullDto> getLocations(Pageable pageable) {
        List<Location> locationList = locationRepository.findAllByStatus(LocationStatus.APPROVED,
        pageable);
        return locationList.stream().map(locationMapper::toLocationFullDto).collect(Collectors.toList());
    }

}
