package com.trackkar.gatestatus.service.interfaces;

import com.trackkar.gatestatus.entity.Feedback;
import com.trackkar.gatestatus.dto.FeedbackRequest;

import java.util.List;
import java.util.UUID;

public interface FeedbackService {
    Feedback submitFeedback(FeedbackRequest feedbackRequest);
    List<Feedback> getAllFeedbacks();
    List<Feedback> getFeedbackByGateId(UUID gateId);
}
