package com.trackkar.gatestatus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
        name = "users"
)
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String email;
    private String phoneNumber;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}