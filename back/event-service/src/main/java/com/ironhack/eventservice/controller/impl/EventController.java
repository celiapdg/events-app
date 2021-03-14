package com.ironhack.eventservice.controller.impl;

import com.ironhack.eventservice.dto.EventsDTO;
import com.ironhack.eventservice.model.Events;
import com.ironhack.eventservice.service.impl.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Transactional
public class EventController {

    @Autowired
    EventService eventService;

    /** Find all the public events currently active or pending */
    @GetMapping("/events/board")
    @ResponseStatus(HttpStatus.OK)
    public List<EventsDTO> findEventsBoard(){
        return eventService.findEventsBoard();
    }

    /** Find any existing event by id */
    @GetMapping("/event/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventsDTO findEventById(@PathVariable Long id){
        return eventService.findEventById(id);
    }

    /** Find the events where a user is host */
    @GetMapping("/events/host/{hostId}")
    @ResponseStatus(HttpStatus.OK)
    public List<EventsDTO> findEventsByHostId(@PathVariable Long hostId){
        return eventService.findEventsByHostId(hostId);
    }

    /** Find the events where a user is guest */
    @GetMapping("/events/guest/{guestId}")
    @ResponseStatus(HttpStatus.OK)
    public List<EventsDTO> findEventsByGuestId(@PathVariable Long guestId){
        return eventService.findEventsByGuestId(guestId);
    }

    /** Find all the started events */
    @GetMapping("/events/started")
    @ResponseStatus(HttpStatus.OK)
    public List<EventsDTO> findEventsStarted(){
        return eventService.findEventsStarted();
    }

    /** Create a new event */
    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventsDTO postEvent(@RequestBody EventsDTO eventsDTO){
        return eventService.postEvent(eventsDTO);
    }

    /** Edit an existing event */
    @PutMapping("/events/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public EventsDTO putEvent(@PathVariable Long id, @RequestBody EventsDTO eventsDTO){
        return eventService.putEvent(id, eventsDTO);
    }

    /** Close an event registration */
    @PutMapping("/events/close/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean closeRegistration(@PathVariable Long id){
        return eventService.closeRegistration(id);
    }

    /** Open an event registration */
    @PutMapping("/events/open/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean openRegistration(@PathVariable Long id){
        return eventService.openRegistration(id);
    }

    /** Cancel an event */
    @PutMapping("/events/cancel/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean cancelEvent(@PathVariable Long id){
        return eventService.cancelEvent(id);
    }
}
