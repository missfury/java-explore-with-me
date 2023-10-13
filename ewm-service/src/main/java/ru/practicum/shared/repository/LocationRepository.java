package ru.practicum.shared.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shared.model.Location;
import ru.practicum.shared.util.enums.LocationStatus;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Location findByNameIgnoreCase(String name);

    Location findByLatAndLon(Float lat, Float lon);

    List<Location> findAllByStatus(LocationStatus status, Pageable pageable);

}
