package com.trackkar.gatestatus.controller;

import com.trackkar.gatestatus.common.LoginRequest;
import com.trackkar.gatestatus.entity.User;
import com.trackkar.gatestatus.service.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        System.out.println("Generated UUID: " + id);
        User user = userService.getUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {

        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID id){
        User user = userService.getUserById(id);
        if(user != null){
            userService.deleteUser(id);
            return ResponseEntity.ok("Successfully deleted the user.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest);
        boolean success = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return success ? ResponseEntity.ok("Login successful")
                : ResponseEntity.status(401).body("Invalid credentials");
    }
}
