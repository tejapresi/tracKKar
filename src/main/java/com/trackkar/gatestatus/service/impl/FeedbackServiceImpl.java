package com.trackkar.gatestatus.service.impl;

import com.trackkar.gatestatus.entity.Feedback;
import com.trackkar.gatestatus.repository.FeedbackRepository;
import com.trackkar.gatestatus.service.interfaces.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FeedbackServiceImpl implements FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Override
    public Feedback submitFeedback(Feedback feedback) {
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
