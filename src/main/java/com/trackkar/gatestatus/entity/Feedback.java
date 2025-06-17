package com.trackkar.gatestatus.entity;

import jakarta.persistence.*;
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
@Entity
@Table(
        name = "feedbacks"
)
public class Feedback {
    @Id
    private UUID id;

    @ManyToOne
    private Gate gate;

    private String message;
    private Instant timestamp;
    private String userAgent;
}