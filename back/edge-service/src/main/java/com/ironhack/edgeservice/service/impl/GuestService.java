package com.ironhack.edgeservice.service.impl;

import com.ironhack.edgeservice.classes.GuestEventKey;
import com.ironhack.edgeservice.clients.EventsClient;
import com.ironhack.edgeservice.clients.UsersClient;
import com.ironhack.edgeservice.dto.EventsDTO;
import com.ironhack.edgeservice.dto.GuestDTO;
import com.ironhack.edgeservice.dto.UserDTO;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GuestService {

    @Autowired
    UsersClient usersClient;
    @Autowired
    EventsClient eventsClient;
    @Autowired
    EventsService eventsService;

    private final CircuitBreakerFactory circuitBreakerFactory = new Resilience4JCircuitBreakerFactory();
    CircuitBreaker guestsCircuit = circuitBreakerFactory.create("events-service");

    /** add the current user as guest to an existing event */
    public GuestDTO addGuest(Long eventId) {
        UserDTO user = checkCurrentUser();
        GuestDTO guestDTO = guestsCircuit.run(() -> eventsClient.addGuest(new GuestEventKey(user.getId(), eventId),"Bearer "+ AuthService.getEventsAuthOk()),
                throwable -> (GuestDTO) guestsFallback(throwable));
        guestDTO.setUsername(user.getUsername());

        return guestDTO;
    }

    /** get the current user's details as guest in a given event */
    public GuestDTO accessGuestDetails(Long eventId){
        UserDTO user = checkCurrentUser();
        GuestDTO guestDTO = guestsCircuit.run(() -> eventsClient.checkGuest(eventId, user.getId(),"Bearer "+AuthService.getEventsAuthOk()),
                throwable -> (GuestDTO) guestsFallback(throwable));
        if (guestDTO!=null) guestDTO.setUsername(user.getUsername());
        return guestDTO;
    }

    /** get the whole list of an event's guests ordered by queue position */
    public List<GuestDTO> accessGuestList(Long eventId) {
        UserDTO user = checkCurrentUser();
        List<GuestDTO> guests = guestsCircuit.run(() -> eventsClient.getEventGuests(eventId, "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> (List<GuestDTO>) guestsFallback(throwable));
        for (GuestDTO guest: guests){
            guest.setUsername(usersClient.findById(guest.getId(), "Bearer "+AuthService.getUsersAuthOk()).getUsername());
        }
        return guests;
    }


    /** get a list of guests currently allowed to visit an event */
    public List<GuestDTO> getNextVisitors(Long eventId) {
        UserDTO user = checkCurrentUser();
        List<GuestDTO> guests = guestsCircuit.run(() -> eventsClient.getNextVisitors(eventId, "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> (List<GuestDTO>) guestsFallback(throwable));
        for (GuestDTO guest: guests){
            guest.setUsername(usersClient.findById(guest.getId(), "Bearer "+AuthService.getUsersAuthOk()).getUsername());
        }
        return guests;
    }

    /** kicks a guest from an event. Can only be performed by the host */
    public void kickGuest(GuestEventKey guestEventKey) {
        UserDTO user = checkCurrentUser();
        EventsDTO eventsDTO = eventsService.accessEventDetails(guestEventKey.getEventId());
        if (user.getId()!=eventsDTO.getHostId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Current user is not the event host");
        }
        guestsCircuit.run(() -> eventsClient.kickGuest(guestEventKey, "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> guestsFallback(throwable));
    }

    /** current user leaves an event */
    public void leave(Long eventId) {
        UserDTO user = checkCurrentUser();
        guestsCircuit.run(() -> eventsClient.leave(new GuestEventKey(user.getId(), eventId), "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> guestsFallback(throwable));
    }

    /** current user sets his status as ready */
    public void guestReady(Long eventId) {
        UserDTO user = checkCurrentUser();
        guestsCircuit.run(() -> eventsClient.guestReady(new GuestEventKey(user.getId(), eventId), "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> guestsFallback(throwable));
    }

    /** current user sets his status as visiting */
    public void guestVisiting(Long eventId) {
        UserDTO user = checkCurrentUser();
        guestsCircuit.run(() -> eventsClient.guestVisiting(new GuestEventKey(user.getId(), eventId), "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> guestsFallback(throwable));
    }

    /** current user sets his status as visited */
    public void guestVisited(Long eventId) {
        UserDTO user = checkCurrentUser();
        guestsCircuit.run(() -> eventsClient.guestVisited(new GuestEventKey(user.getId(), eventId), "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> guestsFallback(throwable));
    }

    private UserDTO checkCurrentUser(){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return usersClient.findByUsername(currentUser, "Bearer "+AuthService.getUsersAuthOk());
    }

    /** fallback when an exception is thrown by the events service, or it's not available */
    private Object guestsFallback(Throwable throwable){
        if (throwable instanceof FeignException){
            if (((FeignException) throwable).status()==404){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Guest doesn't exist");
            }else if(((FeignException) throwable).status()==400){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid guest input data");
            }else{
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The events service is not available right now");
            }

        }else{
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The events service is not available right now");
        }
    }

}
