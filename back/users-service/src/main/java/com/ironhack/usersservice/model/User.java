package com.ironhack.usersservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String username;
    protected String email;
    protected String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore
    protected Set<Role> roles = new HashSet<>();

    /**------------------------Constructors------------------------**/

    /**
     * Default class constructor
     **/
    public User() {
    }

    /**
     * Class constructor specifying name, username and password.
     **/
    public User(String username, String password) {
        this.username = username;
        setPassword(password);
    }

    /**
     * Class constructor specifying name, username, email and password.
     **/
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        setPassword(password);
    }

    /**------------------------Getters and Setters------------------------**/

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

    // automatically encrypts the secret key
    public void setPassword(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("antes "+this.password);
        this.password = passwordEncoder.encode(password);
        System.out.println("encoded "+this.password);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }
}