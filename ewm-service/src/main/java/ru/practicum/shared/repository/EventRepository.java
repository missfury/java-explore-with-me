package ru.practicum.ewmservice.shared.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewmservice.shared.model.Event;
import ru.practicum.ewmservice.shared.util.Pagination;
import ru.practicum.ewmservice.shared.util.enums.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findEventByIdAndInitiatorId(long eventId, long userId);

    List<Event> findEventsByInitiatorId(long userId, Pageable pageable);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE e.category.id = :categoryId")
    List<Event> findFirstByOrderByCategoryAsc(long categoryId, Pageable pageable);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE e.id IN :eventIds")
    List<Event> findEventsByIds(List<Long> eventIds);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE LOWER(e.annotation) LIKE %:text% OR LOWER(e.description) LIKE %:text% " +
            "AND (:categories is null OR e.category.id IN (:categories)) " +
            "AND e.paid = :paid " +
            "AND (:state is null OR e.state = :state) " +
            "AND (coalesce(:start, 'null') is null OR e.eventDate >= :start) " +
            "AND (coalesce(:end, 'null') is null OR e.eventDate <= :end)")
    List<Event> findPublicEvents(String text, List<Long> categories, boolean paid, LocalDateTime start,
                                 LocalDateTime end, State state, Pageable pageable);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE (:users is null OR e.initiator.id IN (:users)) " +
            "AND (:states is null OR e.state IN (:states)) " +
            "AND (:categories is null OR e.category.id IN (:categories)) " +
            "AND (coalesce(:start, 'null') is null OR e.eventDate >= :start) " +
            "AND (coalesce(:end, 'null') is null OR e.eventDate <= :end) ")
    List<Event> findAdminEvents(List<Long> users, List<State> states, List<Long> categories,
                                LocalDateTime start, LocalDateTime end, Pageable pageable);

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

    @Query("SELECT event FROM Event AS event " +
            "WHERE (:text is null OR LOWER(event.annotation) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "AND (:categories is null OR event.category.id IN (:categories)) " +
            "AND (:paid is null OR event.paid = :paid) " +
            "AND (:state is null OR event.state = :state) " +
            "AND (cast(:start as timestamp) is null OR event.eventDate >= :start) " +
            "AND (cast(:end as timestamp) is null OR event.eventDate <= :end) ")
    List<Event> findPublicEvents(String text, List<Long> categories, Boolean paid, LocalDateTime start,
                                 LocalDateTime end, State state, Pageable pageable);

    List<Event> findEventsByCategoryId(long catId);

    @Query("SELECT e FROM Event e " +
            "WHERE " +
            "(" +
            ":text IS NULL " +
            "OR LOWER(e.description) LIKE CONCAT('%', :text, '%') " +
            "OR LOWER(e.annotation) LIKE CONCAT('%', :text, '%')" +
            ")" +
            "AND (:states IS NULL OR e.state IN (:states)) " +
            "AND (:categories IS NULL OR e.category.id IN (:categories)) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (CAST(:rangeStart AS date) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (CAST(:rangeEnd AS date) IS NULL OR e.eventDate <= :rangeEnd) " +
            "order by e.eventDate")
    List<Event> findByParamsOrderByDate(
            @Param("text") String text,
            @Param("states") List<State> states,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);




}
