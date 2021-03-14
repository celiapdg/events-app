package com.ironhack.edgeservice.clients;

import com.ironhack.edgeservice.auth.dto.LoginRequest;
import com.ironhack.edgeservice.classes.GuestEventKey;
import com.ironhack.edgeservice.dto.EventsDTO;
import com.ironhack.edgeservice.dto.GuestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("events-service")
public interface EventsClient {

    @RequestMapping(value = "events/authenticate", method = RequestMethod.POST)
    ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest loginRequest);

    @GetMapping("/events/board")
    List<EventsDTO> findEventsBoard(@RequestHeader(value = "Authorization") String authorizationHeader);

    @GetMapping("/event/{id}")
    EventsDTO findEventById(@PathVariable Long id, @RequestHeader(value = "Authorization") String authorizationHeader);

    @GetMapping("/events/host/{hostId}")
    List<EventsDTO> findEventsByHostId(@PathVariable Long hostId, @RequestHeader(value = "Authorization") String authorizationHeader);

    @GetMapping("/events/guest/{guestId}")
    List<EventsDTO> findEventsByGuestId(@PathVariable Long guestId, @RequestHeader(value = "Authorization") String authorizationHeader);

    @GetMapping("/events/started")
    List<EventsDTO> findEventsStarted(@RequestHeader(value = "Authorization") String authorizationHeader);

    @PostMapping("/events")
    EventsDTO postEvent(@RequestBody EventsDTO eventsDTO, @RequestHeader(value = "Authorization") String authorizationHeader);

    @PutMapping("/events/{id}")
    EventsDTO putEvent(@PathVariable Long id, @RequestBody EventsDTO eventsDTO, @RequestHeader(value = "Authorization") String authorizationHeader);

    @PutMapping("/events/close/{id}")
    Boolean closeRegistration(@PathVariable Long id, @RequestHeader(value = "Authorization") String authorizationHeader);

    @PutMapping("/events/open/{id}")
    Boolean openRegistration(@PathVariable Long id, @RequestHeader(value = "Authorization") String authorizationHeader);

    @PutMapping("/events/cancel/{id}")
    Boolean cancelEvent(@PathVariable Long id, @RequestHeader(value = "Authorization") String authorizationHeader);


    /********************* GUEST METHODS *********************/
    @PostMapping("/guest/")
    GuestDTO addGuest(@RequestBody GuestEventKey guestEventKey, @RequestHeader(value = "Authorization") String authorizationHeader);

    @GetMapping("/guest/{eventId}/{guestId}")
    GuestDTO checkGuest(@PathVariable Long eventId, @PathVariable Long guestId, @RequestHeader(value = "Authorization") String authorizationHeader);

    @GetMapping("/guests/{eventId}")
    List<GuestDTO> getEventGuests(@PathVariable Long eventId, @RequestHeader(value = "Authorization") String authorizationHeader);

    @GetMapping("/guests/next/{eventId}")
    List<GuestDTO> getNextVisitors(@PathVariable Long eventId, @RequestHeader(value = "Authorization") String authorizationHeader);

    @PutMapping("/guests/kick")
    Boolean kickGuest(@RequestBody GuestEventKey guestEventKey, @RequestHeader(value = "Authorization") String authorizationHeader);

    @PutMapping("/guests/ready")
    Boolean guestReady(@RequestBody GuestEventKey guestEventKey, @RequestHeader(value = "Authorization") String authorizationHeader);

    @PutMapping("/guests/visiting")
    Boolean guestVisiting(@RequestBody GuestEventKey guestEventKey, @RequestHeader(value = "Authorization") String authorizationHeader);

    @PutMapping("/guests/visited")
    Boolean guestVisited(@RequestBody GuestEventKey guestEventKey, @RequestHeader(value = "Authorization") String authorizationHeader);

    @DeleteMapping("/guests/leave")
    Boolean leave(@RequestBody GuestEventKey guestEventKey, @RequestHeader(value = "Authorization") String authorizationHeader);
}
