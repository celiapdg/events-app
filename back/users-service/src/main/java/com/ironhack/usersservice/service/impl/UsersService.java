package com.ironhack.usersservice.service.impl;

import com.ironhack.usersservice.dto.UserDTO;
import com.ironhack.usersservice.model.Role;
import com.ironhack.usersservice.model.User;
import com.ironhack.usersservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsersService {

    @Autowired
    UserRepository userRepository;

    public Boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public UserDTO findByUsername(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return UserDTO.parseFromUser(user);
    }

    public UserDTO findById(Long id){
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return UserDTO.parseFromUser(user);
    }

    public UserDTO newUser(UserDTO userDTO) {
        User user = new User(userDTO.getUsername(), userDTO.getEmail(),userDTO.getPassword());
        for (String role: userDTO.getRoles()){
            user.addRole(new Role(role, user));
        }
        userRepository.save(user);
        return UserDTO.parseFromUser(user);
    }
}
