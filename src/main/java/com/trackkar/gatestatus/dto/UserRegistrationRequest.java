package com.trackkar.gatestatus.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationRequest {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format. Please provide a valid email address")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 128, message = "Password must be between 6 and 128 characters long")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(
        regexp = "^[+]?[0-9]{10,15}$", 
        message = "Phone number must be between 10-15 digits and can optionally start with +"
    )
    private String phoneNumber;

    @NotBlank(message = "Role is required")
    @Pattern(
        regexp = "^(ADMIN|GATEKEEPER)$", 
        message = "Role must be either 'ADMIN' or 'GATEKEEPER'"
    )
    private String role;
}
