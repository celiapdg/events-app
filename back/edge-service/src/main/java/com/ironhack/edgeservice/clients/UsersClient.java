package com.ironhack.edgeservice.clients;

import com.ironhack.edgeservice.auth.dto.LoginRequest;
import com.ironhack.edgeservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient("users-service")
public interface UsersClient {

    @RequestMapping(value = "users/authenticate", method = RequestMethod.POST)
    ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest loginRequest);


    @GetMapping("/user/check/{username}")
    Boolean existsByUsername(@PathVariable String username, @RequestHeader(value = "Authorization") String authorizationHeader);

    @GetMapping("/user/check/email/{email}")
    Boolean existsByEmail(@PathVariable String email, @RequestHeader(value = "Authorization") String authorizationHeader);

    @GetMapping("/user/{id}")
    UserDTO findById(@PathVariable Long id, @RequestHeader(value = "Authorization") String authorizationHeader);

    @GetMapping("/user/username/{username}")
    public UserDTO findByUsername(@PathVariable String username, @RequestHeader(value = "Authorization") String authorizationHeader);

    @PostMapping("/user")
    UserDTO newUser(@RequestBody UserDTO userDTO, @RequestHeader(value = "Authorization") String authorizationHeader);

}
