package com.ironhack.edgeservice.controller.impl;

import com.ironhack.edgeservice.dto.EventsDTO;
import com.ironhack.edgeservice.service.impl.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class EventsController {

    @Autowired
    EventsService eventsService;

    /** Find all the public events currently active or pending */
    @GetMapping("/events/board")
    @ResponseStatus(HttpStatus.OK)
    public List<EventsDTO> findEventsBoard() {
        return eventsService.findEventsBoard();
    }

    /** Find any existing event by id */
    @GetMapping("/events/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventsDTO findEventById(@PathVariable Long id){
        return eventsService.findEventById(id);
    }

    /** Find the events where a user is host */
    @GetMapping("/events/host")
    @ResponseStatus(HttpStatus.OK)
    public List<EventsDTO> findEventsByHostId(){
        return eventsService.findEventsByHostId();
    }

    /** Find the events where a user is guest */
    @GetMapping("/events/guest")
    @ResponseStatus(HttpStatus.OK)
    public List<EventsDTO> findEventsByGuestId(){
        return eventsService.findEventsByGuestId();
    }

    /** Find all the started events */
    @GetMapping("/events/started")
    @ResponseStatus(HttpStatus.OK)
    public List<EventsDTO> findEventsStarted(){
        return eventsService.findEventsStarted();
    }

    /** Create a new event */
    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventsDTO postEvent(@RequestBody EventsDTO eventsDTO){
        return eventsService.postEvent(eventsDTO);
    }

    /** Edit an existing event */
    @PutMapping("/events/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public EventsDTO putEvent(@PathVariable Long id, @RequestBody EventsDTO eventsDTO){
        return eventsService.putEvent(id, eventsDTO);
    }

    /** Close an event registration: it will not accept more guests */
    @PutMapping("/events/close/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void closeRegistration(@PathVariable Long id){
        eventsService.closeRegistration(id);
    }

    /** Open an event registration */
    @PutMapping("/events/open/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void openRegistration(@PathVariable Long id){
        eventsService.openRegistration(id);
    }

    /** Cancel an event */
    @PutMapping("/events/cancel/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelEvent(@PathVariable Long id){
        eventsService.cancelEvent(id);
    }
}