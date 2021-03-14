package com.ironhack.eventservice.controller.impl;

import com.ironhack.eventservice.classes.GuestEventKey;
import com.ironhack.eventservice.dto.GuestDTO;
import com.ironhack.eventservice.enums.GuestStatus;
import com.ironhack.eventservice.service.impl.GuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@Transactional
public class GuestController {

    @Autowired
    GuestService guestService;

    /** adds a new guest to an existing event */
    @PostMapping("/guest")
    @ResponseStatus(HttpStatus.CREATED)
    public GuestDTO addGuest(@RequestBody GuestEventKey guestEventKey){
        return guestService.addGuest(guestEventKey);
    }

    /** gets a guest's details */
    @GetMapping("/guest/{eventId}/{guestId}")
    @ResponseStatus(HttpStatus.OK)
    public GuestDTO checkGuest(@PathVariable Long eventId, @PathVariable Long guestId){
        return guestService.getGuestInfo(eventId, guestId);
    }


    /** gets the whole list of an event's guests */
    @GetMapping("/guests/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<GuestDTO> getEventGuests(@PathVariable Long eventId){
        return guestService.getEventGuests(eventId);
    }


    /** gets every participation as guest of a user */
    @GetMapping("/guests/guest/{guestId}")
    @ResponseStatus(HttpStatus.OK)
    public List<GuestDTO> getGuestEvents(@PathVariable Long guestId){
        return guestService.getGuestEvents(guestId);
    }


    /** gets a list of guests currently allowed to visit an event */
    @GetMapping("/guests/next/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public List<GuestDTO> getNextVisitors(@PathVariable Long eventId){
        return guestService.getNextVisitors(eventId);
    }

    /** kicks a guest from an event */
    @PutMapping("/guests/kick")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Boolean kickGuest(@RequestBody GuestEventKey guestEventKey){
        return guestService.kickGuest(guestEventKey);
    }

    /** a guest set his status as ready */
    @PutMapping("/guests/ready")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Boolean guestReady(@RequestBody GuestEventKey guestEventKey){
        return guestService.guestReady(guestEventKey);
    }

    /** a guest set his status as visiting */
    @PutMapping("/guests/visiting")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Boolean guestVisiting(@RequestBody GuestEventKey guestEventKey){
        return guestService.guestVisiting(guestEventKey);
    }

    /** a guest set his status as visited */
    @PutMapping("/guests/visited")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Boolean guestVisited(@RequestBody GuestEventKey guestEventKey){
        return guestService.guestVisited(guestEventKey);
    }

    /** a guest leaves an event */
    @DeleteMapping("/guests/leave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Boolean leave(@RequestBody GuestEventKey guestEventKey){
        return guestService.leave(guestEventKey);
    }

}
