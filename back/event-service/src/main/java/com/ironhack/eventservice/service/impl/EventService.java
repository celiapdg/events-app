package com.ironhack.eventservice.service.impl;

import com.ironhack.eventservice.dto.EventsDTO;
import com.ironhack.eventservice.enums.EventStatus;
import com.ironhack.eventservice.enums.GuestStatus;
import com.ironhack.eventservice.enums.RegistrationStatus;
import com.ironhack.eventservice.enums.Visibility;
import com.ironhack.eventservice.model.Events;
import com.ironhack.eventservice.model.Guest;
import com.ironhack.eventservice.repository.EventsRepository;
import com.ironhack.eventservice.repository.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    EventsRepository eventsRepository;
    @Autowired
    GuestRepository guestRepository;


    /** Find all the public events currently active or pending */
    public List<EventsDTO> findEventsBoard() {
        this.updatePublicEvents();
        List<Events> events = sortEvents(eventsRepository.findPublicNotFinishedNorCancelled());
        return events.stream().map(event -> EventsDTO.parseFromEvent(event)).collect(Collectors.toList());
    }

    /** Find any existing event by id */
    public EventsDTO findEventById(Long id){
        Events event = this.updateSingleEvent(id);
        return EventsDTO.parseFromEvent(event);
    }

    /** Find the events where a user is host */
    public List<EventsDTO> findEventsByHostId(Long hostId){
        List<Events> events = sortEvents(eventsRepository.findByHostId(hostId));
        events = eventsRepository.saveAll(events.stream().map(event -> this.updateEvent(event)).collect(Collectors.toList()));
        return events.stream().map(event -> EventsDTO.parseFromEvent(event)).collect(Collectors.toList());
    }

    /** Find the events where a user is guest */
    public List<EventsDTO> findEventsByGuestId(Long guestId){
        List<Events> events = sortEvents(eventsRepository.findEventsByGuestId(guestId));
        events = eventsRepository.saveAll(events.stream().map(event -> this.updateEvent(event)).collect(Collectors.toList()));
        return events.stream().map(event -> EventsDTO.parseFromEvent(event)).collect(Collectors.toList());
    }

    /** Find all the started events */
    public List<EventsDTO> findEventsStarted(){
        this.updatePublicEvents();
        List<Events> events =  sortEvents(eventsRepository.findByEventStatusAndVisibilityAndRegistrationStatus(EventStatus.STARTED,
                                                                                                    Visibility.PUBLIC,
                                                                                                    RegistrationStatus.OPEN));
        return events.stream().map(event -> EventsDTO.parseFromEvent(event)).collect(Collectors.toList());
    }

    /** Create a new event */
    public EventsDTO postEvent(EventsDTO eventsDTO){
        Events event = Events.parseFromDTO(eventsDTO);
        // let's make sure that the data is correct
        event = this.updateEvent(event);
        event = eventsRepository.save(event);
        return EventsDTO.parseFromEvent(event);
    }

    /** Edit an existing event */
    public EventsDTO putEvent(Long id, EventsDTO eventsDTO){
        Events event = eventsRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Event id not found"));
        // the new total guest limit cannot be less than the current number of guests
        if (eventsDTO.getTotalGuestLimit() < event.getCurrentTotalGuests()){
            eventsDTO.setTotalGuestLimit(event.getCurrentTotalGuests());
        }
        // setting the id, just in case
        eventsDTO.setId(id);
        event = Events.updateFromDTO(event, eventsDTO);
        event = this.updateEvent(event);
        event = eventsRepository.save(event);
        return EventsDTO.parseFromEvent(event);
    }

    /** Close an event registration: it will not accept more guests */
    public boolean closeRegistration(Long id){
        Events event = eventsRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with id "+id+" not found"));
        // closing is only possible if the event is active
        if (event.getEventStatus()==EventStatus.CANCELLED||event.getEventStatus()==EventStatus.FINISHED){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This event has already concluded");
        }else if (event.getRegistrationStatus()==RegistrationStatus.CLOSED_BY_HOST){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This event is already closed");
        }
        event.setRegistrationStatus(RegistrationStatus.CLOSED_BY_HOST);
        eventsRepository.save(event);
        return true;
    }

    /** Open an event registration */
    public boolean openRegistration(Long id){
        Events event = eventsRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with id "+id+" not found"));
        // opening is only possible if the event is active
        if (event.getEventStatus()==EventStatus.CANCELLED||event.getEventStatus()==EventStatus.FINISHED){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This event has already concluded");
        }else if (event.getRegistrationStatus()==RegistrationStatus.OPEN){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This event is already opened");
        }
        event.setRegistrationStatus(RegistrationStatus.OPEN);
        eventsRepository.save(event);
        return true;
    }

    /** Cancel an event */
    public boolean cancelEvent(Long id){
        Events event = eventsRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with id "+id+" not found"));
        if (event.getEventStatus()==EventStatus.CANCELLED||event.getEventStatus()==EventStatus.FINISHED){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This event has already concluded");
        }
        event.setEventStatus(EventStatus.CANCELLED);
        // all the guests who did not visit will change its status to CANCELLED
        List<Guest> guests = guestRepository.findByEventId(id);
        for (Guest guest: guests){
            if (guest.getGuestStatus()!=GuestStatus.VISITED){
                guest.setGuestStatus(GuestStatus.CANCELLED);
            }
        }
        eventsRepository.save(event);
        guestRepository.saveAll(guests);
        return true;
    }

    /** Update an event status, registration status and guest limit */
    public Events updateEvent(Events event){
        Integer visited = guestRepository.countByEventIdAndGuestStatus(event.getId(), GuestStatus.VISITED);
        // update event status, only if the event is not CANCELLED
        if (event.getEventStatus()!=EventStatus.CANCELLED){
            // ending date in the past OR number of guests VISITED = number of max guests accepted
            if (event.getEnding().isBefore(LocalDateTime.now())||event.getTotalGuestLimit()<=visited){
                event.setEventStatus(EventStatus.FINISHED);
            // reached start date, but entry code was not provided
            }else if (event.getOpening().isBefore(LocalDateTime.now())&&event.getEntryCode().isEmpty()){
                event.setEventStatus(EventStatus.PENDING_CODE);
            // reached start date, and entry code was provided
            }else if (event.getOpening().isBefore(LocalDateTime.now())&&!event.getEntryCode().isEmpty()){
                event.setEventStatus(EventStatus.STARTED);
            // none of the above
            }else{
                event.setEventStatus(EventStatus.NOT_STARTED);
            }
        }

        // update registration status
        // closed by host when the event is private, has been cancelled or the host has already closed it
        if (event.getVisibility()==Visibility.PRIVATE||event.getEventStatus()==EventStatus.CANCELLED||event.getRegistrationStatus()==RegistrationStatus.CLOSED_BY_HOST){
            event.setRegistrationStatus(RegistrationStatus.CLOSED_BY_HOST);
        // max guests limit reached or event finished
        }else if(event.getCurrentTotalGuests()>=event.getTotalGuestLimit()||event.getEventStatus()==EventStatus.FINISHED){
            event.setRegistrationStatus(RegistrationStatus.CLOSED);
        // none of the above
        }else{
            event.setRegistrationStatus(RegistrationStatus.OPEN);
        }

        // public events have at least 1 spot for guests, no empty public events allowed
        if (event.getCurrentTotalGuests()==0&&event.getVisibility()==Visibility.PUBLIC){
            event.setGuestLimit(1);
            event.setTotalGuestLimit(1);
        }

        return event;
    }

    /** maps the updateEvents method to a list and saves the result in the database */
    private List<Events> updatePublicEvents(){
        List<Events> events = eventsRepository.findByVisibility(Visibility.PUBLIC);
        return eventsRepository.saveAll(events.stream().map(event -> this.updateEvent(event)).collect(Collectors.toList()));
    }

    /** applies the updateEvents method to a event by id and saves the result in the database */
    private Events updateSingleEvent(Long id){
        Optional<Events> eventsOptional = eventsRepository.findById(id);
        if (eventsOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event with id "+id+" not found");
        }
        return eventsRepository.save(this.updateEvent(eventsOptional.get()));
    }

    /** sort a list of events by start datetime */
    private List<Events> sortEvents(List<Events> events){
        Collections.sort(events, Comparator.comparing(Events::getOpening));
        return events;
    }
}
