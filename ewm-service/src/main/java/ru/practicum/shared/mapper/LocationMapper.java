package ru.practicum.shared.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shared.dto.LocationFullDto;
import ru.practicum.shared.dto.NewLocationDto;
import ru.practicum.shared.dto.ShortLocationDto;
import ru.practicum.shared.model.Location;

@RequiredArgsConstructor
@Component
public class LocationMapper {
    public ShortLocationDto toShortLocationDto(Location location) {
        return ShortLocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public Location toLocation(NewLocationDto newLocationDto) {
        return Location.builder()
                .name(newLocationDto.getName())
                .address(newLocationDto.getAddress())
                .lat(newLocationDto.getLat())
                .lon(newLocationDto.getLon())
                .radius(newLocationDto.getRadius())
                .build();
    }

    public LocationFullDto toNewLocationDto(Location location) {
        return LocationFullDto.builder()
                .id(location.getId())
                .name(location.getName())
                .address(location.getAddress())
                .lat(location.getLat())
                .lon(location.getLon())
                .status(location.getStatus())
                .radius(location.getRadius())
                .build();
    }

    public LocationFullDto toLocationFullDto(Location location) {
        return LocationFullDto.builder()
                .id(location.getId())
                .name(location.getName())
                .address(location.getAddress())
                .lat(location.getLat())
                .lon(location.getLon())
                .radius(location.getRadius())
                .status(location.getStatus())
                .build();
    }
}
