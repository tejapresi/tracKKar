package com.trackkar.gatestatus.service.impl;

import com.trackkar.gatestatus.entity.Feedback;
import com.trackkar.gatestatus.repository.FeedbackRepository;
import com.trackkar.gatestatus.service.interfaces.FeedbackService;
import com.trackkar.gatestatus.dto.FeedbackRequest;
import com.trackkar.gatestatus.repository.GateRepository;
import com.trackkar.gatestatus.entity.Gate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private GateRepository gateRepository;

    @Override
    public Feedback submitFeedback(FeedbackRequest feedbackRequest) {
        // Fetch the Gate entity by ID
        Gate gate = gateRepository.findById(feedbackRequest.getGateId())
            .orElseThrow(() -> new IllegalArgumentException("Gate not found with ID: " + feedbackRequest.getGateId()));
        Feedback feedback = Feedback.builder()
            .id(UUID.randomUUID())
            .gate(gate)
            .message(feedbackRequest.getMessage())
            .timestamp(feedbackRequest.getTimestamp())
            .userAgent(feedbackRequest.getUserAgent())
            .build();
        return feedbackRepository.save(feedback);
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @Override
    public List<Feedback> getFeedbackByGateId(UUID gateId) {
        return feedbackRepository.findByGateId(gateId);
    }

}
