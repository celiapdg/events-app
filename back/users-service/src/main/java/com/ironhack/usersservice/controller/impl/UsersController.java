package com.ironhack.usersservice.controller.impl;

import com.ironhack.usersservice.auth.dto.AuthenticationRequest;
import com.ironhack.usersservice.auth.dto.AuthenticationResponse;
import com.ironhack.usersservice.auth.security.MyUserDetailsService;
import com.ironhack.usersservice.auth.utils.JwtUtils;
import com.ironhack.usersservice.dto.UserDTO;
import com.ironhack.usersservice.service.impl.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class UsersController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    UsersService usersService;

    /** authenticate into this app */
    @RequestMapping(value = "/users/authenticate", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @GetMapping("/user/check/{username}")
    public Boolean existsByUsername(@PathVariable String username){
        return usersService.existsByUsername(username);
    }

    @GetMapping("/user/check/email/{email}")
    public Boolean existsByEmail(@PathVariable String email){
        return usersService.existsByEmail(email);
    }

    @GetMapping("/user/{id}")
    public UserDTO findById(@PathVariable Long id){
        return usersService.findById(id);
    }

    @GetMapping("/user/username/{username}")
    public UserDTO findByUsername(@PathVariable String username){
        return usersService.findByUsername(username);
    }

    @PostMapping("/user")
    public UserDTO newUser(@RequestBody UserDTO userDTO){
        return usersService.newUser(userDTO);
    }
}
