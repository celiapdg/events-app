package com.ironhack.edgeservice.service.impl;

import com.ironhack.edgeservice.clients.EventsClient;
import com.ironhack.edgeservice.clients.UsersClient;
import com.ironhack.edgeservice.dto.UserDTO;
import com.ironhack.edgeservice.service.impl.AuthService;
import com.ironhack.edgeservice.dto.EventsDTO;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class EventsService {

    @Autowired
    EventsClient eventsClient;
    @Autowired
    UsersClient usersClient;

    private final CircuitBreakerFactory circuitBreakerFactory = new Resilience4JCircuitBreakerFactory();
    CircuitBreaker eventsCircuit = circuitBreakerFactory.create("events-service");
    CircuitBreaker usersCircuit = circuitBreakerFactory.create("users-service");

    /** Find all the public events currently active or pending */
    public List<EventsDTO> findEventsBoard() {
        List<EventsDTO> events = eventsCircuit.run(() -> eventsClient.findEventsBoard("Bearer "+AuthService.getEventsAuthOk()),
                throwable -> (List<EventsDTO>) eventsFallback(throwable));
        // add the host name to the output
        for (EventsDTO event: events){
            UserDTO host = usersClient.findById(event.getHostId(), "Bearer "+AuthService.getUsersAuthOk());
            event.setHostName(host.getUsername());
        }
        return events;
    }

    /** Find any existing event by id */
    public EventsDTO findEventById(Long id){
        EventsDTO eventsDTO = this.accessEventDetails(id);
        UserDTO host = usersClient.findById(eventsDTO.getHostId(), "Bearer "+AuthService.getUsersAuthOk());
        eventsDTO.setHostName(host.getUsername());
        return eventsDTO;
    }


    /** Find the events where a user is host */
    public List<EventsDTO> findEventsByHostId() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO user = usersClient.findByUsername(currentUser, "Bearer "+AuthService.getUsersAuthOk());
        List<EventsDTO> events = eventsCircuit.run(() -> eventsClient.findEventsByHostId(user.getId(),"Bearer "+AuthService.getEventsAuthOk()),
                throwable -> (List<EventsDTO>) eventsFallback(throwable));
        // add the host name to the output
        for (EventsDTO event: events){
            UserDTO host = usersClient.findById(event.getHostId(), "Bearer "+AuthService.getUsersAuthOk());
            event.setHostName(host.getUsername());
        }
        return events;
    }

    /** Find the events where a user is guest */
    public List<EventsDTO> findEventsByGuestId() {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO user = usersClient.findByUsername(currentUser, "Bearer "+AuthService.getUsersAuthOk());
        List<EventsDTO> events = eventsCircuit.run(() -> eventsClient.findEventsByGuestId(user.getId(),"Bearer "+AuthService.getEventsAuthOk()),
                throwable -> (List<EventsDTO>) eventsFallback(throwable));
        // add the host name to the output
        for (EventsDTO event: events){
            UserDTO host = usersClient.findById(event.getHostId(), "Bearer "+AuthService.getUsersAuthOk());
            event.setHostName(host.getUsername());
        }
        return events;
    }

    /** Find all the started events */
    public List<EventsDTO> findEventsStarted() {
        List<EventsDTO> events = eventsCircuit.run(() -> eventsClient.findEventsStarted("Bearer "+AuthService.getEventsAuthOk()),
                throwable -> (List<EventsDTO>) eventsFallback(throwable));
        // add the host name to the output
        for (EventsDTO event: events){
            UserDTO host = usersClient.findById(event.getHostId(), "Bearer "+AuthService.getUsersAuthOk());
            event.setHostName(host.getUsername());
        }
        return events;
    }

    /** Create a new event */
    public EventsDTO postEvent(EventsDTO eventsDTO) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDTO user = usersClient.findByUsername(currentUser, "Bearer "+AuthService.getUsersAuthOk());
        eventsDTO.setHostId(user.getId());
        EventsDTO eventsDTO_final = eventsCircuit.run(() -> eventsClient.postEvent(eventsDTO, "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> (EventsDTO) eventsFallback(throwable));
        eventsDTO_final.setHostName(user.getUsername());
        return eventsDTO_final;
    }

    /** Edit an existing event */
    public EventsDTO putEvent(Long id, EventsDTO eventsDTO) {
        EventsDTO dbEventsDTO = this.accessEventDetails(id);
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        // current user must be the host to edit an event
        UserDTO user = usersClient.findByUsername(currentUser, "Bearer "+AuthService.getUsersAuthOk());
        if (user.getId()!=dbEventsDTO.getHostId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Current user is not the event host");
        }
        // setting the host id, just in case
        eventsDTO.setHostId(dbEventsDTO.getHostId());

        EventsDTO eventsDTO_final = eventsCircuit.run(() -> eventsClient.putEvent(id, eventsDTO, "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> (EventsDTO) eventsFallback(throwable));
        eventsDTO_final.setHostName(SecurityContextHolder.getContext().getAuthentication().getName());
        return eventsDTO_final;
    }


    /** Close an event registration: it will not accept more guests */
    public void closeRegistration(Long id) {
        this.accessEventDetails(id);
        eventsCircuit.run(() -> eventsClient.closeRegistration(id, "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> eventsFallback(throwable));
    }

    /** Open an event registration */
    public void openRegistration(Long id) {
        this.accessEventDetails(id);
        eventsCircuit.run(() -> eventsClient.openRegistration(id, "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> eventsFallback(throwable));
    }


    /** Cancel an event */
    public void cancelEvent(Long id) {
        this.accessEventDetails(id);
        eventsCircuit.run(() -> eventsClient.cancelEvent(id, "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> eventsFallback(throwable));
    }

    /** check if the logged use has acces to an event's details */
    public EventsDTO accessEventDetails(Long id){
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        EventsDTO eventsDTO = eventsCircuit.run(() -> eventsClient.findEventById(id, "Bearer "+AuthService.getEventsAuthOk()),
                throwable -> (EventsDTO) eventsFallback(throwable));
        if (eventsDTO.getVisibility().equals("PRIVATE")){
            UserDTO user = usersClient.findByUsername(currentUser, "Bearer "+AuthService.getUsersAuthOk());
            // if current user is not the host of the private event
            if (user.getId()!=eventsDTO.getHostId()){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Current user is not the event host");
            }
        }
        return eventsDTO;
    }

    /** fallback when an exception is thrown by the events service, or it's not available */
    private Object eventsFallback(Throwable throwable){
        if (throwable instanceof FeignException){
            if (((FeignException) throwable).status()==404){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event doesn't exist");
            }else if(((FeignException) throwable).status()==400){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid event input data");
            }else{
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The events service is not available right now");
            }

        }else{
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The events service is not available right now");
        }
    }
}
