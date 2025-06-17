package com.trackkar.gatestatus.service.interfaces;

import com.trackkar.gatestatus.dto.UserRegistrationRequest;
import com.trackkar.gatestatus.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(UserRegistrationRequest request);
    List<User> getAllUsers();
    User getUserById(UUID id);
    void deleteUser(UUID id);
    String login(String email, String password);
}
