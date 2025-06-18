package com.trackkar.gatestatus.controller;

import com.trackkar.gatestatus.dto.ErrorResponse;
import com.trackkar.gatestatus.dto.GateAssignmentRequest;
import com.trackkar.gatestatus.dto.GateCreateRequest;
import com.trackkar.gatestatus.dto.GateResponse;
import com.trackkar.gatestatus.entity.Gate;
import com.trackkar.gatestatus.entity.GateStatus;
import com.trackkar.gatestatus.service.interfaces.GateService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gates")
public class GateController {
    private final GateService gateService;

    public GateController(GateService gateService) {
        this.gateService = gateService;
    }

    @GetMapping
    public ResponseEntity<?> getAllGates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) {
        
        try {
            Pageable pageable = PageRequest.of(page, size, 
                sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
            
            Page<Gate> gatePage = gateService.getGatesWithPagination(pageable, status, search);
            
            List<GateResponse> gateResponses = gatePage.getContent().stream()
                    .map(GateResponse::fromGate)
                    .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("data", gateResponses);
            response.put("metadata", Map.of(
                "currentPage", gatePage.getNumber(),
                "totalPages", gatePage.getTotalPages(),
                "totalGates", gatePage.getTotalElements(),
                "size", gatePage.getSize(),
                "hasNext", gatePage.hasNext(),
                "hasPrevious", gatePage.hasPrevious()
            ));
            
            if (status != null) {
                response.put("filteredBy", status);
            }
            if (search != null) {
                response.put("searchTerm", search);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(Instant.now())
                    .status(400)
                    .error("Invalid Parameters")
                    .message(e.getMessage())
                    .path("/api/gates")
                    .details(Map.of("validStatuses", "OPEN, CLOSED"))
                    .build();
            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGateById(@PathVariable UUID id) {
        Gate gate = gateService.getGateById(id);
        if (gate != null) {
            return ResponseEntity.ok(Map.of("gate", GateResponse.fromGate(gate)));
        } else {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(Instant.now())
                    .status(404)
                    .error("Gate Not Found")
                    .message("Gate not found")
                    .path("/api/gates/" + id)
                    .details(Map.of("requestedGateId", id.toString()))
                    .build();
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<?> createGate(@Valid @RequestBody GateCreateRequest request) {
        Gate gate = gateService.createGate(request);
        return ResponseEntity.status(201).body(Map.of("gate", GateResponse.fromGate(gate)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateGateStatus(
            @PathVariable UUID id,
            @RequestParam GateStatus status,
            @RequestParam UUID updatedBy) {
        
        try {
            Gate gate = gateService.updateGateStatus(id, status, updatedBy);
            return ResponseEntity.ok(Map.of("gate", GateResponse.fromGate(gate)));
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(Instant.now())
                    .status(400)
                    .error("Update Failed")
                    .message(e.getMessage())
                    .path("/api/gates/" + id + "/status")
                    .details(Map.of("error", e.getMessage()))
                    .build();
            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGate(@PathVariable UUID id) {
        boolean deleted = gateService.deleteGate(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "Gate deleted successfully"));
        } else {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(Instant.now())
                    .status(404)
                    .error("Gate Not Found")
                    .message("Gate not found to delete")
                    .path("/api/gates/" + id)
                    .details(Map.of("requestedGateId", id.toString()))
                    .build();
            return ResponseEntity.status(404).body(errorResponse);
        }
    }

    @PostMapping("/assign")
    public ResponseEntity<?> assignGatekeeperToGate(@Valid @RequestBody GateAssignmentRequest request) {
        try {
            boolean assigned = gateService.assignGatekeeperToGate(request);
            return ResponseEntity.ok(Map.of("message", "Gatekeeper assigned to gate successfully"));
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(Instant.now())
                    .status(400)
                    .error("Assignment Failed")
                    .message(e.getMessage())
                    .path("/api/gates/assign")
                    .details(Map.of("error", e.getMessage()))
                    .build();
            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @DeleteMapping("/unassign/{gatekeeperId}")
    public ResponseEntity<?> unassignGatekeeperFromGate(@PathVariable UUID gatekeeperId) {
        try {
            boolean unassigned = gateService.unassignGatekeeperFromGate(gatekeeperId);
            return ResponseEntity.ok(Map.of("message", "Gatekeeper unassigned from gate successfully"));
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(Instant.now())
                    .status(400)
                    .error("Unassignment Failed")
                    .message(e.getMessage())
                    .path("/api/gates/unassign/" + gatekeeperId)
                    .details(Map.of("error", e.getMessage()))
                    .build();
            return ResponseEntity.status(400).body(errorResponse);
        }
    }

    @GetMapping("/assigned")
    public ResponseEntity<?> getGatesWithAssignedGatekeepers() {
        List<Gate> gates = gateService.getGatesWithAssignedGatekeepers();
        List<GateResponse> gateResponses = gates.stream()
                .map(GateResponse::fromGate)
                .collect(Collectors.toList());
        return ResponseEntity.ok(Map.of("gates", gateResponses));
    }

    @GetMapping("/gatekeeper/{gatekeeperId}")
    public ResponseEntity<?> getGateByGatekeeper(@PathVariable UUID gatekeeperId) {
        try {
            Gate gate = gateService.getGateByGatekeeper(gatekeeperId);
            if (gate != null) {
                return ResponseEntity.ok(Map.of("gate", GateResponse.fromGate(gate)));
            } else {
                return ResponseEntity.ok(Map.of("message", "Gatekeeper is not assigned to any gate"));
            }
        } catch (Exception e) {
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(Instant.now())
                    .status(400)
                    .error("Gatekeeper Not Found")
                    .message(e.getMessage())
                    .path("/api/gates/gatekeeper/" + gatekeeperId)
                    .details(Map.of("error", e.getMessage()))
                    .build();
            return ResponseEntity.status(400).body(errorResponse);
        }
    }
}
