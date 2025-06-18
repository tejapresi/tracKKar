package com.trackkar.gatestatus.controller;

import com.trackkar.gatestatus.dto.ErrorResponse;
import com.trackkar.gatestatus.dto.UserLoginRequest;
import com.trackkar.gatestatus.dto.UserLoginResponse;
import com.trackkar.gatestatus.dto.UserRegistrationRequest;
import com.trackkar.gatestatus.entity.User;
import com.trackkar.gatestatus.service.interfaces.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String search) {
        
        try {
            Pageable pageable = PageRequest.of(page, size, 
                sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
            
            Page<User> userPage = userService.getUsersWithPagination(pageable, role, search);
            
            Map<String, Object> response = new HashMap<>();
            response.put("data", userPage.getContent());
            response.put("metadata", Map.of(
                "currentPage", userPage.getNumber(),
                "totalPages", userPage.getTotalPages(),
                "totalUsers", userPage.getTotalElements(),
                "size", userPage.getSize(),
                "hasNext", userPage.hasNext(),
                "hasPrevious", userPage.hasPrevious()
            ));
            
            if (role != null) {
                response.put("filteredBy", role);
            }
            if (search != null) {
                response.put("searchTerm", search);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(Instant.now())
                    .status(400)
                    .error("Invalid Parameters")
                    .message(e.getMessage())
                    .path("/api/users")
                    .details(Map.of("validRoles", "ADMIN, GATEKEEPER"))
                    .build();
            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(Map.of("user", user));
        } else {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(Instant.now())
                    .status(404)
                    .error("User Not Found")
                    .message("User not found")
                    .path("/api/users/" + id)
                    .details(Map.of("requestedUserId", id.toString()))
                    .build();
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        User user = userService.createUser(request);
        return ResponseEntity.status(201).body(Map.of("user", user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } else {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(Instant.now())
                    .status(404)
                    .error("User Not Found")
                    .message("User not found to delete")
                    .path("/api/users/" + id)
                    .details(Map.of("requestedUserId", id.toString()))
                    .build();
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest loginRequest) {
        try {
            UserLoginResponse response = userService.login(
                loginRequest.getEmail(),
                loginRequest.getPhoneNumber(),
                loginRequest.getPassword()
            );
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(Instant.now())
                    .status(401)
                    .error("Authentication Failed")
                    .message(ex.getMessage())
                    .path("/api/users/login")
                    .details(Map.of("error", ex.getMessage()))
                    .build();
            return ResponseEntity.status(401).body(errorResponse);
        }
    }
}
