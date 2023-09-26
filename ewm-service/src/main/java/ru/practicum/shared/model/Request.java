package ru.practicum.shared.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shared.util.enums.RequestStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.practicum.shared.util.enums.RequestStatus.PENDING;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "created", updatable = false, nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime created = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private RequestStatus status = PENDING;
}
