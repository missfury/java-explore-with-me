package ru.practicum.shared.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shared.model.Event;
import ru.practicum.shared.util.Pagination;
import ru.practicum.shared.util.enums.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findEventByIdAndInitiatorId(long eventId, long userId);

    List<Event> findEventsByInitiatorId(long userId, Pageable pageable);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE e.id IN :eventIds")
    List<Event> findEventsByIds(List<Long> eventIds);

    @Query(
            "SELECT e " +
                    "FROM Event e " +
                    "JOIN FETCH e.initiator i " +
                    "JOIN FETCH e.category c " +
                    "WHERE e.state = :state " +
                    "AND (:categories IS NULL OR c.id IN :categories) " +
                    "AND e.eventDate > :rangeStart " +
                    "AND (:paid IS NULL OR e.paid = :paid) " +
                    "AND (:text IS NULL OR " +
                    "UPPER(e.annotation) LIKE UPPER(CONCAT('%', :text, '%')) " +
                    "OR UPPER(e.description) LIKE UPPER(CONCAT('%', :text, '%')) " +
                    "OR UPPER(e.title) LIKE UPPER(CONCAT('%', :text, '%')))"
    )
    List<Event> findAllPublishStateAvailable(
            @Param("state") State state,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("text") String text,
            Pagination pageable
    );

    @Query(
            "SELECT e " +
                    "FROM Event e " +
                    "JOIN FETCH e.initiator " +
                    "JOIN FETCH e.category c " +
                    "WHERE e.state = :state " +
                    "AND (:categories IS NULL OR c.id IN :categories) " +
                    "AND e.eventDate > :rangeStart " +
                    "AND (:paid IS NULL OR e.paid = :paid) " +
                    "AND (:text IS NULL OR UPPER(e.annotation) LIKE UPPER(:searchPattern) " +
                    "OR UPPER(e.description) LIKE UPPER(:searchPattern))"
    )
    List<Event> findAllPublishStateNotAvailable(
            @Param("state") State state,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("text") String text,
            Pagination pageable
    );

    @Query("SELECT event FROM Event AS event " +
            "WHERE (:users is null OR event.initiator.id IN (:users)) " +
            "AND (:states is null OR event.state IN (:states)) " +
            "AND (:categories is null OR event.category.id IN (:categories)) " +
            "AND (cast(:start as timestamp) is null OR event.eventDate >= :start) " +
            "AND (cast(:end as timestamp) is null OR event.eventDate <= :end) ")
    List<Event> findEventsByFilters(List<Long> users, List<State> states, List<Long> categories,
                                    LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE FUNCTION('distance', :lat, :lon, e.location.lat, e.location.lon) " +
            "<= :radius " +
            "AND e.state = :state " +
            "ORDER BY e.eventDate DESC ")
    List<Event> findEventsFromLocationRadius(
            @Param("lat") Float lat,
            @Param("lon") Float lon,
            @Param("radius") Float radius,
            State state,
            Pageable pageable);

    List<Event> findByLocationIdAndState(Long locationId, State state, Pageable pageable);


}
