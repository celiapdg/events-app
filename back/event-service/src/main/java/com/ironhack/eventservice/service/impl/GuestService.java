package com.ironhack.eventservice.service.impl;

import com.ironhack.eventservice.classes.GuestEventKey;
import com.ironhack.eventservice.dto.GuestDTO;
import com.ironhack.eventservice.enums.EventStatus;
import com.ironhack.eventservice.enums.GuestStatus;
import com.ironhack.eventservice.enums.RegistrationStatus;
import com.ironhack.eventservice.model.Events;
import com.ironhack.eventservice.model.Guest;
import com.ironhack.eventservice.repository.EventsRepository;
import com.ironhack.eventservice.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuestService {

    @Autowired
    GuestRepository guestRepository;
    @Autowired
    EventsRepository eventsRepository;
    @Autowired
    EventService eventService;

    /** add a new guest to an existing event */
    public GuestDTO addGuest(GuestEventKey guestEventKey){
        Events event = this.checkValidEvent(guestEventKey.getEventId());

        if (isHost(guestEventKey.getGuestId(),event.getHostId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are the host of this event");
        }

        if (isGuest(guestEventKey.getEventId(), guestEventKey.getGuestId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are already a guest in this event");
        }

        Guest guest = new Guest(guestEventKey, event, GuestStatus.REGISTERED, event.getCurrentTotalGuests()+1);
        guest = guestRepository.save(guest);
        // update the total guests count
        event.setCurrentTotalGuests(event.getCurrentTotalGuests()+1);
        eventsRepository.save(eventService.updateEvent(event));
        return GuestDTO.parseFromGuest(guest);
    }

    /** get a guest's details */
    public GuestDTO getGuestInfo(Long eventId, Long guestId){
        if (isGuest(eventId, guestId)){
            return GuestDTO.parseFromGuest(guestRepository.findById(new GuestEventKey(guestId, eventId)).get());
        }
        return null;
    }

    /** get the whole list of an event's guests ordered by queue position */
    public List<GuestDTO> getEventGuests(Long eventId) {
        List<Guest> guests = sortGuests(guestRepository.findByEventId(eventId));
        return guests.stream().map(guest -> GuestDTO.parseFromGuest(guest)).collect(Collectors.toList());
    }

    /** gets every participation as guest of a user */
    public List<GuestDTO> getGuestEvents(Long guestId) {
        List<Guest> guests = guestRepository.findByIdGuestId(guestId);
        return guests.stream().map(guest -> GuestDTO.parseFromGuest(guest)).collect(Collectors.toList());
    }

    /** get a list of guests currently allowed to visit an event */
    public List<GuestDTO> getNextVisitors(Long eventId) {
        Events event = eventsRepository.findById(eventId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        event = eventService.updateEvent(event);
        if (event.getEventStatus()!= EventStatus.STARTED){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This event has not started!");
        }

        // checks if there are free spots for READY guests
        List<Guest> visiting = guestRepository.findByEventIdAndGuestStatus(eventId, GuestStatus.VISITING);
        List<Guest> ready = sortGuests(guestRepository.findByEventIdAndGuestStatus(eventId, GuestStatus.READY));
        while ((event.getGuestLimit() - visiting.size() > 0) && (ready.size() > 0)){
            visiting.add(ready.remove(0));
        }
        // update the event's queue position
        Integer newQueuePosition = 0;
        for (Guest guest: visiting){
            if (guest.getQueuePosition()>newQueuePosition){
                newQueuePosition = guest.getQueuePosition();
            }
        }
        event.setCurrentQueuePosition(newQueuePosition);
        eventsRepository.save(event);
        return visiting.stream().map(guest -> GuestDTO.parseFromGuest(guest)).collect(Collectors.toList());
    }

    /** kicks a guest from an event */
    public Boolean kickGuest(GuestEventKey guestEventKey){
        this.updateGuestStatus(guestEventKey, GuestStatus.KICKED_OUT);
        Events event = eventsRepository.findById(guestEventKey.getEventId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        event.setCurrentTotalGuests(event.getCurrentTotalGuests()-1);
        eventsRepository.save(eventService.updateEvent(event));
        return true;
    }

    /** a guest leaves an event */
    public Boolean leave(GuestEventKey guestEventKey){
        Guest traitor = guestRepository.findById(guestEventKey).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest not found"));
        Integer queuePosition = traitor.getQueuePosition();
        guestRepository.deleteById(guestEventKey);
        Events event = eventsRepository.findById(guestEventKey.getEventId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        event.getGuestList().remove(traitor);
        List<Guest> guests = event.getGuestList();
        for (Guest guest: guests){
            if (guest.getQueuePosition()>queuePosition){
                guest.setQueuePosition(guest.getQueuePosition()-1);
            }
        }
        // update the total guests count
        event.setCurrentTotalGuests(event.getCurrentTotalGuests()-1);
        if (guests.size()>0){
            guestRepository.saveAll(guests);
        }
        eventsRepository.save(eventService.updateEvent(event));
        return true;
    }

    /** a guest set his status as ready */
    public Boolean guestReady(GuestEventKey guestEventKey){
        this.updateGuestStatus(guestEventKey, GuestStatus.READY);
        return true;
    }

    /** a guest set his status as visiting */
    public Boolean guestVisiting(GuestEventKey guestEventKey){
        this.updateGuestStatus(guestEventKey, GuestStatus.VISITING);
        return true;
    }

    /** a guest set his status as visited */
    public Boolean guestVisited(GuestEventKey guestEventKey){
        this.updateGuestStatus(guestEventKey, GuestStatus.VISITED);
        return true;
    }

    /** method to change the status of a guest */
    private void updateGuestStatus(GuestEventKey guestEventKey, GuestStatus guestStatus){
        Events event = eventsRepository.findById(guestEventKey.getEventId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        event = eventService.updateEvent(event);
        // not possible to set a status as READY, VISITING of VISITED if the event status is not STARTED
        if (event.getEventStatus()!=EventStatus.STARTED&&(guestStatus==GuestStatus.READY||
                guestStatus==GuestStatus.VISITING||guestStatus==GuestStatus.VISITED)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This event has not started!");
        }
        Guest guest = guestRepository.findById(guestEventKey).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest not found"));
        guest.setGuestStatus(guestStatus);
        guestRepository.save(guest);
    }

    /** method to check if the current user id is the host id */
    private Boolean isHost(Long hostId, Long userId){
        return hostId.equals(userId);
    }

    /** method to check if the current user is guest */
    private Boolean isGuest(Long eventId, Long userId){
        List<Guest> guestList = guestRepository.findByIdGuestId(userId);
        for (Guest guest: guestList){
            if (guest.getId().getEventId() == eventId){
                return true;
            }
        }
        return false;
    }

    /** check if an event has its registration OPEN */
    private Events checkValidEvent(Long id){
        Events event = eventsRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        if (!event.getRegistrationStatus().equals(RegistrationStatus.OPEN)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This event does not accept more guests");
        }
        return event;
    }

    /** order a list of guests by queue position */
    private List<Guest> sortGuests(List<Guest> guests){
        Collections.sort(guests, Comparator.comparing(Guest::getQueuePosition));
        return guests;
    }

}
