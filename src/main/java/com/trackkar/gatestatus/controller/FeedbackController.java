package com.trackkar.gatestatus.controller;

import com.trackkar.gatestatus.entity.Feedback;
import com.trackkar.gatestatus.service.interfaces.FeedbackService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Feedback>> getAllFeedback() {
        return ResponseEntity.ok(feedbackService.getAllFeedbacks());
    }

    @PostMapping
    public ResponseEntity<Feedback> submitFeedback(@RequestBody Feedback feedback) {
        return ResponseEntity.ok(feedbackService.submitFeedback(feedback));
    }

    @GetMapping("/gate/{gateId}")
    public ResponseEntity<List<Feedback>> getFeedbackForGate(@PathVariable UUID gateId) {
        return ResponseEntity.ok(feedbackService.getFeedbackByGateId(gateId));
    }
}
