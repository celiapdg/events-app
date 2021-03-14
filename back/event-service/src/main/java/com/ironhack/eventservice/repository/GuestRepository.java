package com.ironhack.eventservice.repository;

import com.ironhack.eventservice.classes.GuestEventKey;
import com.ironhack.eventservice.enums.GuestStatus;
import com.ironhack.eventservice.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<Guest, GuestEventKey> {

    List<Guest> findByEventId(Long id);

    List<Guest> findByIdGuestId(Long id);

    List<Guest> findByEventIdAndGuestStatus(Long eventId, GuestStatus guestStatus);

    Integer countByEventIdAndGuestStatus(Long id, GuestStatus visited);
}
