package com.trackkar.gatestatus.config;

import com.trackkar.gatestatus.entity.User;
import com.trackkar.gatestatus.entity.UserRole;
import com.trackkar.gatestatus.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
public class DevTestingConfig {
    @Bean
    CommandLineRunner init(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            if (userRepository.findByEmail("admin@example.com").isEmpty()) {
                User admin = User.builder()
                        .name("Admin User")
                        .email("admin@example.com")
                        .phoneNumber("9999999999")
                        .password(encoder.encode("admin123"))
                        .role(UserRole.ADMIN)
                        .build();
                userRepository.save(admin);
                System.out.println("Admin user created");
            }
        };
    }

}
