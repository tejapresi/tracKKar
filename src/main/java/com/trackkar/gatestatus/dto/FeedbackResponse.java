package com.trackkar.gatestatus.dto;

import com.trackkar.gatestatus.entity.Feedback;
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
public class FeedbackResponse {
    private UUID id;
    private GateSummary gate;
    private String message;
    private Instant timestamp;
    private String userAgent;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GateSummary {
        private UUID id;
        private String name;
        private String status;
    }

    public static FeedbackResponse fromFeedback(Feedback feedback) {
        FeedbackResponse response = FeedbackResponse.builder()
                .id(feedback.getId())
                .message(feedback.getMessage())
                .timestamp(feedback.getTimestamp())
                .userAgent(feedback.getUserAgent())
                .build();

        if (feedback.getGate() != null) {
            response.setGate(GateSummary.builder()
                    .id(feedback.getGate().getId())
                    .name(feedback.getGate().getName())
                    .status(feedback.getGate().getStatus().name())
                    .build());
        }

        return response;
    }
} 