package com.trackkar.gatestatus.service.interfaces;

import com.trackkar.gatestatus.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(User user);
    List<User> getAllUsers();
    User getUserById(UUID id);
    void deleteUser(UUID id);
    boolean login(String email, String password);
}
