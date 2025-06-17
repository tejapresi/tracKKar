package com.trackkar.gatestatus.service.interfaces;

import com.trackkar.gatestatus.entity.Feedback;

import java.util.List;
import java.util.UUID;

public interface FeedbackService {
    Feedback submitFeedback(Feedback feedback);
    List<Feedback> getAllFeedbacks();
    List<Feedback> getFeedbackByGateId(UUID gateId);
}
