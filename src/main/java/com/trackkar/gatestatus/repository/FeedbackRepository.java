package com.trackkar.gatestatus.repository;

import com.trackkar.gatestatus.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {
    List<Feedback> findByGateId(UUID gateId);
}