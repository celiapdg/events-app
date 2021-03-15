package com.ironhack.usersservice.dto;

import com.ironhack.usersservice.model.Role;
import com.ironhack.usersservice.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private List<String> roles = new ArrayList<>();

    public static UserDTO parseFromUser(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        for (Role role: user.getRoles()){
            userDTO.addRole(role.getName());
        }
        return userDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void addRole(String role) {
        this.roles.add(role);
    }
}
