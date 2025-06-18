package com.trackkar.gatestatus.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GateAssignmentRequest {
    
    @NotNull(message = "Gatekeeper ID is required")
    private UUID gatekeeperId;
    
    @NotNull(message = "Gate ID is required")
    private UUID gateId;
} 