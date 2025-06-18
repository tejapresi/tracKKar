package com.trackkar.gatestatus.security;

import com.trackkar.gatestatus.config.JwtConfig;
import com.trackkar.gatestatus.entity.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Autowired
    private JwtConfig jwtConfig;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(String email, String role) {
        long expirationTime = getExpirationTimeForRole(role);
        
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private long getExpirationTimeForRole(String role) {
        try {
            UserRole userRole = UserRole.valueOf(role.toUpperCase());
            switch (userRole) {
                case ADMIN:
                    return jwtConfig.getExpiry().getAdmin() * 1000; // Convert seconds to milliseconds
                case GATEKEEPER:
                    return jwtConfig.getExpiry().getGatekeeper() * 1000; // Convert seconds to milliseconds
                default:
                    return jwtConfig.getExpiry().getGatekeeper() * 1000; // Default to gatekeeper expiry
            }
        } catch (IllegalArgumentException e) {
            // If role is invalid, default to gatekeeper expiry
            return jwtConfig.getExpiry().getGatekeeper() * 1000;
        }
    }

    public boolean isTokenValid(String token, String email) {
        final String username = extractUsername(token);
        return (username.equals(email) && !isTokenExpired(token));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
