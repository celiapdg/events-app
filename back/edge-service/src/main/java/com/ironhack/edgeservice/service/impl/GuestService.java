package com.ironhack.edgeservice.service.impl;

import com.ironhack.edgeservice.classes.GuestEventKey;
import com.ironhack.edgeservice.clients.EventsClient;
import com.ironhack.edgeservice.service.impl.AuthService;
import com.ironhack.edgeservice.dto.EventsDTO;
import com.ironhack.edgeservice.dto.GuestDTO;
import com.ironhack.edgeservice.model.User;
import com.ironhack.edgeservice.repository.UserRepository;
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
    UserRepository userRepository;
    @Autowired
    EventsClient eventsClient;
    @Autowired
    EventsService eventsService;

    private final CircuitBreakerFactory circuitBreakerFactory = new Resilience4JCircuitBreakerFactory();
    CircuitBreaker guestsCircuit = circuitBreakerFactory.create("events-service");

    /** add the current user as guest to an existing event */
    public GuestDTO addGuest(Long eventId) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentUser).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current user is not in user database"));
        GuestDTO guestDTO = guestsCircuit.run(() -> eventsClient.addGuest(new GuestEventKey(user.getId(), eventId),"Bearer "+ AuthService.getEventsAuthOk()),
                throwable -> (GuestDTO) guestsFallback(throwable));
        guestDTO.setUsername(user.getUsername());

        return guestDTO;
    }

    /** get the current user's details as guest in a given event */
    public GuestDTO accessGuestDetails(Long eventId){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentUser).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current user is not in user database"));
        GuestDTO guestDTO = guestsCircuit.run(() -> eventsClient.checkGuest(eventId, user.getId(),"Bearer "+AuthService.getEventsAuthOk()),
                throwable -> (GuestDTO) guestsFallback(throwable));
        if (guestDTO!=null) guestDTO.setUsername(user.getUsername());
        return guestDTO;
    }

    /** get the whole list of an event's guests ordered by queue position */
    public List<GuestDTO> accessGuestList(Long eventId) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentUser).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current user is not in user database"));
        List<GuestDTO> guests = guestsCircuit.run(() -> eventsClient.getEventGuests(eventId, "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> (List<GuestDTO>) guestsFallback(throwable));
        for (GuestDTO guest: guests){
            guest.setUsername(userRepository.findById(guest.getId()).get().getUsername());
        }
        return guests;
    }


    /** get a list of guests currently allowed to visit an event */
    public List<GuestDTO> getNextVisitors(Long eventId) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentUser).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current user is not in user database"));
        List<GuestDTO> guests = guestsCircuit.run(() -> eventsClient.getNextVisitors(eventId, "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> (List<GuestDTO>) guestsFallback(throwable));
        for (GuestDTO guest: guests){
            guest.setUsername(userRepository.findById(guest.getId()).get().getUsername());
        }
        return guests;
    }

    /** kicks a guest from an event. Can only be performed by the host */
    public void kickGuest(GuestEventKey guestEventKey) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentUser).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current user is not in user database"));
        EventsDTO eventsDTO = eventsService.accessEventDetails(guestEventKey.getEventId());
        if (user.getId()!=eventsDTO.getHostId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Current user is not the event host");
        }
        guestsCircuit.run(() -> eventsClient.kickGuest(guestEventKey, "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> guestsFallback(throwable));
    }

    /** current user leaves an event */
    public void leave(Long eventId) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentUser).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current user is not in user database"));
        guestsCircuit.run(() -> eventsClient.leave(new GuestEventKey(user.getId(), eventId), "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> guestsFallback(throwable));
    }

    /** current user sets his status as ready */
    public void guestReady(Long eventId) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentUser).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current user is not in user database"));
        guestsCircuit.run(() -> eventsClient.guestReady(new GuestEventKey(user.getId(), eventId), "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> guestsFallback(throwable));
    }

    /** current user sets his status as visiting */
    public void guestVisiting(Long eventId) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentUser).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current user is not in user database"));
        guestsCircuit.run(() -> eventsClient.guestVisiting(new GuestEventKey(user.getId(), eventId), "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> guestsFallback(throwable));
    }

    /** current user sets his status as visited */
    public void guestVisited(Long eventId) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(currentUser).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Current user is not in user database"));
        guestsCircuit.run(() -> eventsClient.guestVisited(new GuestEventKey(user.getId(), eventId), "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> guestsFallback(throwable));
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
