package com.trackkar.gatestatus.dto;

import com.trackkar.gatestatus.entity.Gate;
import com.trackkar.gatestatus.entity.GateStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GateResponse {
    private UUID id;
    private String name;
    private Double latitude;
    private Double longitude;
    private GateStatus status;
    private Instant lastUpdated;
    private UserSummary updatedBy;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserSummary {
        private UUID id;
        private String name;
        private String email;
        private String phoneNumber;
        private String role;
    }

    public static GateResponse fromGate(Gate gate) {
        GateResponse response = GateResponse.builder()
                .id(gate.getId())
                .name(gate.getName())
                .latitude(gate.getLatitude())
                .longitude(gate.getLongitude())
                .status(gate.getStatus())
                .lastUpdated(gate.getLastUpdated())
                .build();

        if (gate.getUpdatedBy() != null) {
            response.setUpdatedBy(UserSummary.builder()
                    .id(gate.getUpdatedBy().getId())
                    .name(gate.getUpdatedBy().getName())
                    .email(gate.getUpdatedBy().getEmail())
                    .phoneNumber(gate.getUpdatedBy().getPhoneNumber())
                    .role(gate.getUpdatedBy().getRole().name())
                    .build());
        }

        return response;
    }
} 