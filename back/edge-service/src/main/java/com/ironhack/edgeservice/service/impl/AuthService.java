package com.ironhack.edgeservice.service.impl;

import com.ironhack.edgeservice.auth.dto.LoginRequest;
import com.ironhack.edgeservice.auth.dto.LoginResponse;
import com.ironhack.edgeservice.auth.dto.SignupRequest;
import com.ironhack.edgeservice.auth.security.MyUserDetailsService;
import com.ironhack.edgeservice.auth.utils.JwtUtils;
import com.ironhack.edgeservice.clients.UsersClient;
import com.ironhack.edgeservice.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    UsersClient usersClient;

    private static String eventsAuthOk;
    private static String usersAuthOk;

    /**
     * add a new user into the database
     */
    public ResponseEntity<?> register(@RequestBody SignupRequest signupRequest) throws Exception {
        // username and email must be unique
        if (usersClient.existsByUsername(signupRequest.getUsername(), "Bearer "+ getUsersAuthOk())) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        if (usersClient.existsByEmail(signupRequest.getEmail(),"Bearer "+ getUsersAuthOk())) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        UserDTO user = new UserDTO();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(signupRequest.getPassword());
        user.setEmail(signupRequest.getEmail());

        user.addRole("USER");

        usersClient.newUser(user, "Bearer "+ getUsersAuthOk());

        return ResponseEntity.ok("User registered successfully!");
    }


    /**
     * authenticate
     */
    public ResponseEntity<?> createAuthenticationToken(LoginRequest loginRequest) throws Exception {

        Authentication authentication;
        try {
            // check valid credentials
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Invalid username or password", e);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(loginRequest.getUsername());

        // generate a token to be used as a Authentication header
        final String jwt = jwtUtils.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(jwt));
    }


    /**
     * getter for the events-service authentication key
     */
    public static String getEventsAuthOk() {
        return eventsAuthOk;
    }

    /**
     * setter for the events-service authentication key
     */
    public static void setEventsAuthOk(String eventsAuthOk) {
        AuthService.eventsAuthOk = eventsAuthOk;
    }


    public static String getUsersAuthOk() {
        return usersAuthOk;
    }

    public static void setUsersAuthOk(String usersAuthOk) {
        AuthService.usersAuthOk = usersAuthOk;
    }
}
