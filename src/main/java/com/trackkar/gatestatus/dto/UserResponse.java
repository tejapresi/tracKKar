package com.trackkar.gatestatus.dto;

import com.trackkar.gatestatus.entity.User;
import com.trackkar.gatestatus.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private UserRole role;
    private GateSummary assignedGate;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GateSummary {
        private UUID id;
        private String name;
        private String status;
    }

    public static UserResponse fromUser(User user) {
        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .build();

        if (user.getAssignedGate() != null) {
            response.setAssignedGate(GateSummary.builder()
                    .id(user.getAssignedGate().getId())
                    .name(user.getAssignedGate().getName())
                    .status(user.getAssignedGate().getStatus().name())
                    .build());
        }

        return response;
    }
} 