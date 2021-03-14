package com.ironhack.edgeservice.controller.impl;

import com.ironhack.edgeservice.classes.GuestEventKey;
import com.ironhack.edgeservice.dto.GuestDTO;
import com.ironhack.edgeservice.service.impl.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GuestController {

    @Autowired
    GuestService guestService;

    /** add the current user as guest to an existing event */
    @PostMapping("/guests/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public GuestDTO addGuest(@PathVariable Long eventId){
        return guestService.addGuest(eventId);
    }

    /** get the current user's details as guest in a given event */
    @GetMapping("/guests/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public GuestDTO accessGuestDetails(@PathVariable Long eventId) {
        return guestService.accessGuestDetails(eventId);
    }

    /** get the whole list of an event's guests */
    @GetMapping("/guests/all/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<GuestDTO> accessGuestList(@PathVariable Long eventId) {
        return guestService.accessGuestList(eventId);
    }

    /** get a list of guests currently allowed to visit an event */
    @GetMapping("/guests/next/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<GuestDTO> getNextVisitors(@PathVariable Long eventId){
        return guestService.getNextVisitors(eventId);
    }

    /** kicks a guest from an event */
    @PutMapping("/guests/kick")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void kickGuest(@RequestBody GuestEventKey guestEventKey){
        guestService.kickGuest(guestEventKey);
    }

    /** current user sets his status as ready */
    @PutMapping("/guests/ready/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void guestReady(@PathVariable Long eventId){
        guestService.guestReady(eventId);
    }

    /** current user sets his status as visiting */
    @PutMapping("/guests/visiting/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void guestVisiting(@PathVariable Long eventId){
        guestService.guestVisiting(eventId);
    }

    /** current user sets his status as visited */
    @PutMapping("/guests/visited/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void guestVisited(@PathVariable Long eventId){
        guestService.guestVisited(eventId);
    }

    /** current user leaves an event */
    @DeleteMapping("/guests/leave/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leave(@PathVariable Long eventId){
        guestService.leave(eventId);
    }

    }
