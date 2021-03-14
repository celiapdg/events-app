package com.ironhack.eventservice.repository;

import com.ironhack.eventservice.enums.EventStatus;
import com.ironhack.eventservice.enums.RegistrationStatus;
import com.ironhack.eventservice.enums.Visibility;
import com.ironhack.eventservice.model.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventsRepository extends JpaRepository<Events, Long> {

    List<Events> findByName(String name);

    List<Events> findByHostId(Long id);

    @Query(value = "SELECT * FROM events where (event_status = 'STARTED' OR event_status = 'NOT_STARTED' OR event_status = 'PENDING_CODE') " +
                "AND visibility = 'PUBLIC'",
    nativeQuery = true)
    List<Events> findPublicNotFinishedNorCancelled();

    List<Events> findByVisibility(Visibility visibility);

    List<Events> findByEventStatusAndVisibilityAndRegistrationStatus(EventStatus eventStatus, Visibility visibility, RegistrationStatus registrationStatus);

    @Query(value = "SELECT e.* FROM guest g JOIN events e on g.event_id = e.id where guest_id = :guestId", nativeQuery = true)
    List<Events> findEventsByGuestId(Long guestId);

    List<Events> findByOpeningBetween(LocalDateTime start, LocalDateTime end);

    List<Events> findByEndingBetween(LocalDateTime start, LocalDateTime end);
}
