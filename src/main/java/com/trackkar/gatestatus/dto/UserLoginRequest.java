package com.trackkar.gatestatus.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserLoginRequest {

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(
        regexp = "^[+]?[0-9]{10,15}$",
        message = "Phone number must be between 10 and 15 digits and can optionally start with +"
    )
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    private String password;
}
