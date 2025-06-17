package com.trackkar.gatestatus.config;

import com.trackkar.gatestatus.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF since it's a REST API
                .authorizeHttpRequests(auth -> auth
                        // Public APIs
                        .requestMatchers("/gates/**").permitAll()
                        .requestMatchers("/feedback").permitAll()
                        .requestMatchers("/auth/**").permitAll()

                        // Role-restricted GateController endpoints
                        .requestMatchers(HttpMethod.POST, "/api/gates").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/gates/**/status").hasRole("GATEKEEPER")

                        // Only ADMIN can access the registration endpoint
                        .requestMatchers("/api/users/register").hasRole("ADMIN")

                        // Only Admins can access admin-specific APIs
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Only Gatekeepers can access their update/status APIs
                        .requestMatchers("/api/gatekeeper/**").hasRole("GATEKEEPER")

                        // Everything else must be authenticated
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Needed if we ever want to manually authenticate (not used now)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}