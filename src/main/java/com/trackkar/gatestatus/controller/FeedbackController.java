package com.trackkar.gatestatus.controller;

import com.trackkar.gatestatus.dto.FeedbackResponse;
import com.trackkar.gatestatus.entity.Feedback;
import com.trackkar.gatestatus.service.interfaces.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public ResponseEntity<List<FeedbackResponse>> getAllFeedback() {
        List<FeedbackResponse> feedbackResponses = feedbackService.getAllFeedbacks().stream()
                .map(FeedbackResponse::fromFeedback)
                .collect(Collectors.toList());
        return ResponseEntity.ok(feedbackResponses);
    }

    @PostMapping
    public ResponseEntity<FeedbackResponse> submitFeedback(@RequestBody Feedback feedback) {
        Feedback savedFeedback = feedbackService.submitFeedback(feedback);
        return ResponseEntity.ok(FeedbackResponse.fromFeedback(savedFeedback));
    }

    @GetMapping("/gate/{gateId}")
    public ResponseEntity<List<FeedbackResponse>> getFeedbackForGate(@PathVariable UUID gateId) {
        List<FeedbackResponse> feedbackResponses = feedbackService.getFeedbackByGateId(gateId).stream()
                .map(FeedbackResponse::fromFeedback)
                .collect(Collectors.toList());
        return ResponseEntity.ok(feedbackResponses);
    }
}
