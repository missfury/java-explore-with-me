package ru.practicum.guest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.guest.service.PublicLocationService;
import ru.practicum.shared.dto.LocationFullDto;
import ru.practicum.shared.util.Pagination;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/locations")
public class PublicLocationController {
    private final PublicLocationService publicLocationService;

    @GetMapping("/{id}")
    public LocationFullDto getLocation(@PathVariable long id) {
        LocationFullDto location = publicLocationService.getLocation(id);
        log.info("Получена локация с id= {}",id);
        return location;
    }

    @GetMapping
    public List<LocationFullDto> getLocations(@RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        List<LocationFullDto> locations = publicLocationService.getLocations(new Pagination(from,
                size, Sort.unsorted()));
        log.info("Получен список локаций");
        return locations;
    }
}
