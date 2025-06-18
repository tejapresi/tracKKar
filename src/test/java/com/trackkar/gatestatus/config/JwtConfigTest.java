package com.trackkar.gatestatus.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtConfigTest {

    @Test
    public void testJwtConfigDefaultValues() {
        JwtConfig jwtConfig = new JwtConfig();
        JwtConfig.Expiry expiry = new JwtConfig.Expiry();
        
        // Test default values
        assertEquals(86400L, expiry.getAdmin()); // 24 hours in seconds
        assertEquals(36000L, expiry.getGatekeeper()); // 10 hours in seconds
        
        // Test that we can set and get values
        expiry.setAdmin(72000L);
        expiry.setGatekeeper(18000L);
        
        assertEquals(72000L, expiry.getAdmin());
        assertEquals(18000L, expiry.getGatekeeper());
    }

    @Test
    public void testJwtConfigSecret() {
        JwtConfig jwtConfig = new JwtConfig();
        
        // Test secret setting and getting
        jwtConfig.setSecret("test-secret-key");
        assertEquals("test-secret-key", jwtConfig.getSecret());
        
        // Test that secret is not null after setting
        assertNotNull(jwtConfig.getSecret());
        assertFalse(jwtConfig.getSecret().isEmpty());
    }
} 