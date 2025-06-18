package com.trackkar.gatestatus.validation;

import com.trackkar.gatestatus.dto.UserRegistrationRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserValidationTest {

    private Validator validator;

    @BeforeAll
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidUserRegistration() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("John Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");
        request.setPhoneNumber("1234567890");
        request.setRole("GATEKEEPER");

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Valid request should have no violations");
    }

    @Test
    public void testInvalidEmail() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("John Doe");
        request.setEmail("invalid-email");
        request.setPassword("password123");
        request.setPhoneNumber("1234567890");
        request.setRole("GATEKEEPER");

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Invalid email should have violations");
        
        boolean hasEmailViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));
        assertTrue(hasEmailViolation, "Should have email validation violation");
    }

    @Test
    public void testInvalidPhoneNumber() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("John Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");
        request.setPhoneNumber("123"); // Too short
        request.setRole("GATEKEEPER");

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Invalid phone number should have violations");
        
        boolean hasPhoneViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("phoneNumber"));
        assertTrue(hasPhoneViolation, "Should have phone number validation violation");
    }

    @Test
    public void testValidPhoneNumberWithPlus() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("John Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");
        request.setPhoneNumber("+1234567890");
        request.setRole("GATEKEEPER");

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Valid phone number with + should have no violations");
    }

    @Test
    public void testInvalidRole() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("John Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");
        request.setPhoneNumber("1234567890");
        request.setRole("INVALID_ROLE");

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Invalid role should have violations");
        
        boolean hasRoleViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("role"));
        assertTrue(hasRoleViolation, "Should have role validation violation");
    }

    @Test
    public void testEmptyName() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("");
        request.setEmail("john.doe@example.com");
        request.setPassword("password123");
        request.setPhoneNumber("1234567890");
        request.setRole("GATEKEEPER");

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Empty name should have violations");
        
        boolean hasNameViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name"));
        assertTrue(hasNameViolation, "Should have name validation violation");
    }

    @Test
    public void testShortPassword() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("John Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("123"); // Too short
        request.setPhoneNumber("1234567890");
        request.setRole("GATEKEEPER");

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty(), "Short password should have violations");
        
        boolean hasPasswordViolation = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password"));
        assertTrue(hasPasswordViolation, "Should have password validation violation");
    }

    @Test
    public void testValidAdminRole() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Admin User");
        request.setEmail("admin@example.com");
        request.setPassword("password123");
        request.setPhoneNumber("1234567890");
        request.setRole("ADMIN");

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Valid ADMIN role should have no violations");
    }

    @Test
    public void testValidGatekeeperRole() {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setName("Gatekeeper User");
        request.setEmail("gatekeeper@example.com");
        request.setPassword("password123");
        request.setPhoneNumber("1234567890");
        request.setRole("GATEKEEPER");

        Set<ConstraintViolation<UserRegistrationRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty(), "Valid GATEKEEPER role should have no violations");
    }
} 