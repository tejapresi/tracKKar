package com.trackkar.gatestatus.controller;

import com.trackkar.gatestatus.entity.Gate;
import com.trackkar.gatestatus.entity.GateStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.trackkar.gatestatus.service.interfaces.GateService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gates")
public class GateController {
    private final GateService gateService;

    public GateController(GateService gateService) {
        this.gateService = gateService;
    }

    @GetMapping
    public ResponseEntity<List<Gate>> getAllGates() {
        return ResponseEntity.ok(gateService.getAllGates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Gate> getGateById(@PathVariable UUID id) {
        Gate gate = gateService.getGateById(id);
        return gate != null ? ResponseEntity.ok(gate) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Gate> createGate(@RequestBody Gate gate) {
        return ResponseEntity.ok(gateService.createGate(gate));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Gate> updateGateStatus(@PathVariable UUID id,
                                                 @RequestParam GateStatus status,
                                                 @RequestParam UUID updatedBy) {
        Gate updatedGate = gateService.updateGateStatus(id, status, updatedBy);
        return updatedGate != null ? ResponseEntity.ok(updatedGate) : ResponseEntity.notFound().build();
    }
}
