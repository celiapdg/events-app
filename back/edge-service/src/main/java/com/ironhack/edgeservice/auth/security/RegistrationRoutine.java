package com.ironhack.edgeservice.auth.security;

import com.ironhack.edgeservice.auth.dto.LoginRequest;
import com.ironhack.edgeservice.clients.EventsClient;
import com.ironhack.edgeservice.controller.impl.AuthController;
import com.ironhack.edgeservice.service.impl.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


@Component
public class RegistrationRoutine {

    @Autowired
    EventsClient eventsClient;


    public static boolean isEventsRegistered = false;
    public static boolean isUsersRegistered = false;

    private static final Logger log = LoggerFactory.getLogger(RegistrationRoutine.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private CircuitBreakerFactory circuitBreakerFactory = new Resilience4JCircuitBreakerFactory();

    /** every 10 seconds, the edge service will try to register into its clients */
    @Scheduled(fixedRate = 10000)
    public void checkRegistrationEvents() {
        if (!isEventsRegistered){
            CircuitBreaker circuitBreaker = circuitBreakerFactory.create("events-service");
            log.info("Trying to register with events-service {}", dateFormat.format(new Date()));
            LoginRequest loginRequest = new LoginRequest("edge-service", "edge-service");
            ResponseEntity<?> responseEntity= circuitBreaker.run(() -> eventsClient.createAuthenticationToken(loginRequest), throwable -> fallbackTransaction("events-service"));
            if (responseEntity != null) {
                parseJWTEvents(responseEntity);
                isEventsRegistered = true;
                log.info("Registered with events-service auth token: {}", AuthService.getEventsAuthOk());
            }
        }
    }
/*
    @Scheduled(fixedRate = 10000)
    public void checkRegistrationUsers() {
        if (!isAccountRegistered){
            CircuitBreaker circuitBreaker = circuitBreakerFactory.create("account-service");
            log.info("Trying to register with account-service {}", dateFormat.format(new Date()));
            AuthenticationRequest authenticationRequest = new AuthenticationRequest("opportunity-service", "opportunity-service");
            ResponseEntity<?> responseEntity= circuitBreaker.run(() -> accountClient.createAuthenticationToken(authenticationRequest), throwable -> fallbackTransaction("account-service"));
            if (responseEntity != null) {
                parseJWTAccount(responseEntity);
                isAccountRegistered = true;
                log.info("Registered with account-service auth token: {}", AuthOpportunityController.getAccountAuthOk());
            }
        }
    }
*/
    private void parseJWTEvents(ResponseEntity<?> responseEntity) {
        String auth = Objects.requireNonNull(responseEntity.getBody()).toString();
        AuthService.setEventsAuthOk(auth.substring(5, auth.length() - 1));
    }
/*
    private void parseJWTUsers(ResponseEntity<?> responseEntity) {
        String auth = Objects.requireNonNull(responseEntity.getBody()).toString();
        AuthService.setUsersAuthOk(auth.substring(5, auth.length() - 1));
    }*/

    private ResponseEntity<?> fallbackTransaction(String serviceName) {
        log.info( serviceName + " is not reachable {}", dateFormat.format(new Date()));
        return null;
    }
}