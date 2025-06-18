package com.trackkar.gatestatus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secret;
    private Expiry expiry = new Expiry();

    @Data
    public static class Expiry {
        private long admin = 86400; // 24 hours in seconds (default)
        private long gatekeeper = 36000; // 10 hours in seconds (default)
    }
} 