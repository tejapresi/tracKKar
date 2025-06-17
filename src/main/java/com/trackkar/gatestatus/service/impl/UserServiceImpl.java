package com.trackkar.gatestatus.service.impl;

import com.trackkar.gatestatus.entity.User;
import com.trackkar.gatestatus.repository.UserRepository;
import com.trackkar.gatestatus.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.map(value -> value.getPassword().equals(password)).orElse(false);
    }
}
