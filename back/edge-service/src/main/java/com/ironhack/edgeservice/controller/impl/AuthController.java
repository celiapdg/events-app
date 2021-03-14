package com.ironhack.edgeservice.controller.impl;

import com.ironhack.edgeservice.auth.dto.LoginRequest;
import com.ironhack.edgeservice.auth.dto.SignupRequest;
import com.ironhack.edgeservice.service.impl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    /** add a new user into the database */
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> register(@RequestBody SignupRequest signupRequest)throws Exception {
        return authService.register(signupRequest);
    }

    /** authenticate */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest loginRequest) throws Exception {
        return authService.createAuthenticationToken(loginRequest);
    }

}
