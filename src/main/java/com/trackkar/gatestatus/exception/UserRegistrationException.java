package com.trackkar.gatestatus.exception;

public class UserRegistrationException extends RuntimeException {
    
    private final String field;
    private final String errorCode;
    
    public UserRegistrationException(String message) {
        super(message);
        this.field = null;
        this.errorCode = "USER_REGISTRATION_ERROR";
    }
    
    public UserRegistrationException(String message, String field) {
        super(message);
        this.field = field;
        this.errorCode = "USER_REGISTRATION_ERROR";
    }
    
    public UserRegistrationException(String message, String field, String errorCode) {
        super(message);
        this.field = field;
        this.errorCode = errorCode;
    }
    
    public String getField() {
        return field;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
} 