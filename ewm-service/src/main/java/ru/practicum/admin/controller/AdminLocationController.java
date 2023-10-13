package ru.practicum.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.admin.service.AdminLocationService;
import ru.practicum.shared.dto.LocationFullDto;
import ru.practicum.shared.dto.NewLocationDto;
import ru.practicum.shared.dto.UpdateLocationDto;
import ru.practicum.shared.util.Pagination;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/locations")
public class AdminLocationController {
    private final AdminLocationService adminLocationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationFullDto addLocation(@RequestBody @Valid NewLocationDto newLocationDto) {
        LocationFullDto location = adminLocationService.addLocation(newLocationDto);
        log.info("Создана новая локация = {}", newLocationDto);
        return location;
    }

    @PatchMapping("/{id}")
    public LocationFullDto updateLocation(@PathVariable long id,
                                          @RequestBody @Valid UpdateLocationDto updateLocationDto) {
        LocationFullDto location = adminLocationService.updateLocation(id, updateLocationDto);
        log.info("Обновлена локация с id= {}", id);
        return location;
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable long id) {
        adminLocationService.deleteLocation(id);
        log.info("Удалена локация с id= {}",id);
    }

    @GetMapping
    public List<LocationFullDto> getLocations(@RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        List<LocationFullDto> locations = adminLocationService.getAllLocations(new Pagination(from,
                size, Sort.unsorted()));
        log.info("Получен список локаций");
        return locations;
    }


}
