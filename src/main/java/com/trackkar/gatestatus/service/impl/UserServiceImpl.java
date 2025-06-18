package com.trackkar.gatestatus.service.impl;

import com.trackkar.gatestatus.dto.UserLoginResponse;
import com.trackkar.gatestatus.dto.UserRegistrationRequest;
import com.trackkar.gatestatus.entity.User;
import com.trackkar.gatestatus.entity.UserRole;
import com.trackkar.gatestatus.exception.UserRegistrationException;
import com.trackkar.gatestatus.repository.UserRepository;
import com.trackkar.gatestatus.security.JwtService;
import com.trackkar.gatestatus.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @Override
    public User createUser(UserRegistrationRequest request) {
        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserRegistrationException("Email already registered.", "email", "EMAIL_ALREADY_EXISTS");
        }

        // Check if phone number already exists
        if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new UserRegistrationException("Phone number already registered.", "phoneNumber", "PHONE_ALREADY_EXISTS");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(UserRole.valueOf(request.getRole().toUpperCase()))
                .build();
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Page<User> getUsersWithPagination(Pageable pageable, String role, String search) {
        if (role != null && !role.trim().isEmpty()) {
            try {
                UserRole userRole = UserRole.valueOf(role.toUpperCase());
                if (search != null && !search.trim().isEmpty()) {
                    return userRepository.findByRoleAndNameOrEmailContainingIgnoreCase(userRole, search, pageable);
                } else {
                    return userRepository.findByRole(userRole, pageable);
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role: " + role + ". Valid roles are: ADMIN, GATEKEEPER");
            }
        } else if (search != null && !search.trim().isEmpty()) {
            return userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteUser(UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public UserLoginResponse login(String email, String phoneNumber, String password) {
        Optional<User> userOpt = Optional.empty();
        if (email != null && !email.isBlank()) {
            userOpt = userRepository.findByEmail(email);
        } else if (phoneNumber != null && !phoneNumber.isBlank()) {
            userOpt = userRepository.findByPhoneNumber(phoneNumber);
        }

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Generate JWT token with role-based expiry (handled by JwtService)
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        
        // Return response - JwtService already handles the correct expiry time
        return UserLoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(getExpiryTimeForRole(user.getRole()))
                .build();
    }

    private long getExpiryTimeForRole(UserRole role) {
        // This method now only returns the expiry time in seconds for the response
        // The actual token expiry is handled by JwtService.generateToken()
        switch (role) {
            case ADMIN:
                return 86400; // 24 hours in seconds
            case GATEKEEPER:
                return 36000; // 10 hours in seconds
            default:
                return 36000; // Default to gatekeeper expiry
        }
    }
}
