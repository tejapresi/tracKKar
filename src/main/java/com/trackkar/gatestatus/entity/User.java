package com.trackkar.gatestatus.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format. Please provide a valid email address")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    private String email;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^[+]?[0-9]{10,15}$", 
        message = "Phone number must be between 10-15 digits and can optionally start with +"
    )
    private String phoneNumber;

    @JsonIgnore
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 128, message = "Password must be between 6 and 128 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToOne
    @JoinColumn(name = "assigned_gate")
    private Gate assignedGate;
}