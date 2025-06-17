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
        name = "gates"
)
public class Gate {
    @Id
    private UUID id;
    private String name;
    private Double latitude;
    private Double longitude;

    @Enumerated(EnumType.STRING)
    private GateStatus status;

    private Instant lastUpdated;

    @ManyToOne
    private User updatedBy;
}