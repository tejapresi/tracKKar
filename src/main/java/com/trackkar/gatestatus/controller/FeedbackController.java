package com.trackkar.gatestatus.controller;

import com.trackkar.gatestatus.entity.Feedback;
import com.trackkar.gatestatus.service.interfaces.FeedbackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public List<Feedback> getAllFeedback() {
        return feedbackService.getAllFeedbacks();
    }

    @PostMapping
    public Feedback submitFeedback(@RequestBody Feedback feedback) {
        return feedbackService.submitFeedback(feedback);
    }

    @GetMapping("/gate/{gateId}")
    public List<Feedback> getFeedbackForGate(@PathVariable UUID gateId) {
        return feedbackService.getFeedbackByGateId(gateId);
    }
}
