package ru.practicum.shared.model;

import lombok.*;
import ru.practicum.shared.util.enums.LocationStatus;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location_name", length = 250)
    private String name;

    @Column(name = "address", length = 1000)
    private String address;

    @Column(name = "lat")
    private Float lat;

    @Column(name = "lon")
    private Float lon;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LocationStatus status;

    public Location(Float lat, Float lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
