package ru.practicum.ewmservice.shared.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewmservice.shared.model.Event;
import ru.practicum.ewmservice.shared.model.Request;
import ru.practicum.ewmservice.shared.util.enums.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequesterId(long userId);
    Optional<Request> findByIdAndRequesterId(long requestId, long userId);
    List<Request> findRequestsByEventId(long eventId);
    @Query("SELECT r " +
            "FROM Request AS r " +
            "WHERE r.event.id = :eventId " +
            "AND r.status = :status")
    List<Request> findByEventIdAndStatus(long eventId, RequestStatus status);

    @Query("SELECT r " +
            "FROM Request AS r " +
            "WHERE r.event.id = :eventId " +
            "AND r.id IN (:requestIds)")
    List<Request> findByEventIdAndRequestsIds(long eventId, List<Long> requestIds);

    @Query("SELECT r " +
            "FROM Request AS r " +
            "WHERE r.event.id = :eventId " +
            "AND r.requester.id = :userId")
    Optional<Request> findByEventAndRequesterId(long eventId, long userId);

    @Query("SELECT r " +
            "FROM Request AS r " +
            "WHERE r.event.id = :eventId " +
            "AND r.status = 'CONFIRMED'")
    List<Request> findByEventIdConfirmed(long eventId);

    Long countRequestByEventIdAndStatus(Long eventId, RequestStatus state);
    List<Request> findAllByEventInAndStatus(List<Event> event, RequestStatus status);

}
