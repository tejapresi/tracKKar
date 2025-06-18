package com.trackkar.gatestatus.service.interfaces;

import com.trackkar.gatestatus.dto.UserLoginResponse;
import com.trackkar.gatestatus.dto.UserRegistrationRequest;
import com.trackkar.gatestatus.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(UserRegistrationRequest request);
    List<User> getAllUsers();
    Page<User> getUsersWithPagination(Pageable pageable, String role, String search);
    User getUserById(UUID id);
    boolean deleteUser(UUID id);
    UserLoginResponse login(String email, String phoneNumber, String password);
}
