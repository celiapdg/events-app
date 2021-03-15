package com.ironhack.edgeservice.auth.security;

import com.ironhack.edgeservice.clients.UsersClient;
import com.ironhack.edgeservice.dto.UserDTO;
import com.ironhack.edgeservice.service.impl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UsersClient usersClient;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDTO userDTO = usersClient.findByUsername(username, "Bearer "+ AuthService.getUsersAuthOk());
        return new CustomUserDetails(userDTO);
    }

}
