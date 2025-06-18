package com.trackkar.gatestatus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse {
    private Instant timestamp;
    private int status;
    private String message;
    private String path;
    private Map<String, Object> data;
} 