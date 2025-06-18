package com.trackkar.gatestatus.service.impl;

import com.trackkar.gatestatus.dto.GateAssignmentRequest;
import com.trackkar.gatestatus.dto.GateCreateRequest;
import com.trackkar.gatestatus.entity.Gate;
import com.trackkar.gatestatus.entity.GateStatus;
import com.trackkar.gatestatus.entity.User;
import com.trackkar.gatestatus.entity.UserRole;
import com.trackkar.gatestatus.exception.ResourceNotFoundException;
import com.trackkar.gatestatus.repository.GateRepository;
import com.trackkar.gatestatus.repository.UserRepository;
import com.trackkar.gatestatus.service.interfaces.GateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class GateServiceImpl implements GateService {
    private static final Logger logger = LoggerFactory.getLogger(GateServiceImpl.class);

    @Autowired
    private GateRepository gateRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Gate createGate(GateCreateRequest request) {
        // Set default status if not provided
        GateStatus status = request.getStatus() != null ? request.getStatus() : GateStatus.CLOSED;
        
        Gate gate = Gate.builder()
                .name(request.getName())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .status(status)
                .lastUpdated(Instant.now())
                .build();
        
        // Save the gate first
        gate = gateRepository.save(gate);
        
        // If gatekeeper is provided, assign them to the gate
        if (request.getGatekeeperId() != null) {
            try {
                // Validate gatekeeper exists and is a gatekeeper
                User gatekeeper = userRepository.findById(request.getGatekeeperId())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getGatekeeperId()));

                if (gatekeeper.getRole() != UserRole.GATEKEEPER) {
                    throw new IllegalArgumentException("User must be a GATEKEEPER to be assigned to a gate");
                }

                // Check if gatekeeper is already assigned to another gate
                if (gatekeeper.getAssignedGate() != null) {
                    throw new IllegalArgumentException("Gatekeeper is already assigned to gate: " + gatekeeper.getAssignedGate().getName());
                }

                // Assign gatekeeper to gate
                gatekeeper.setAssignedGate(gate);
                userRepository.save(gatekeeper);
                
            } catch (Exception e) {
                // If assignment fails, still return the created gate but log the error
                // You might want to handle this differently based on your requirements
                logger.error("Failed to assign gatekeeper to gate: ", e);
            }
        }
        
        return gate;
    }

    @Override
    public Gate updateGateStatus(UUID gateId, GateStatus status, UUID updatedBy) {
        Gate gate = gateRepository.findById(gateId)
                .orElseThrow(() -> new ResourceNotFoundException("Gate not found with ID: " + gateId));

        gate.setStatus(status);
        gate.setLastUpdated(Instant.now());
        gate.setUpdatedBy(userRepository.findById(updatedBy)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + updatedBy)));

        return gateRepository.save(gate);
    }

    @Override
    public Gate getGateById(UUID id) {
        return gateRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteGate(UUID id) {
        Gate gate = gateRepository.findById(id).orElse(null);
        if (gate != null) {
            logger.info("Attempting to delete gate: {} ({})", gate.getName(), id);
            
            // First, unassign all users from this gate
            List<User> assignedUsers = userRepository.findAllByAssignedGate(gate);
            if (!assignedUsers.isEmpty()) {
                logger.info("Unassigning {} users from gate: {}", assignedUsers.size(), gate.getName());
                for (User user : assignedUsers) {
                    user.setAssignedGate(null);
                    userRepository.save(user);
                    logger.debug("Unassigned user: {} from gate: {}", user.getName(), gate.getName());
                }
            } else {
                logger.info("No users assigned to gate: {}", gate.getName());
            }
            
            // Now delete the gate
            gateRepository.deleteById(id);
            logger.info("Gate with ID: {} has been deleted", id);
            return true;
        }
        logger.warn("Attempted to delete non-existent gate with ID: {}", id);
        return false;
    }

    @Override
    public Page<Gate> getGatesWithPagination(Pageable pageable, String status, String search) {
        if (status != null && !status.trim().isEmpty()) {
            try {
                GateStatus gateStatus = GateStatus.valueOf(status.toUpperCase());
                return gateRepository.findByStatus(gateStatus, pageable);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + status + ". Valid statuses are: OPEN, CLOSED");
            }
        } else if (search != null && !search.trim().isEmpty()) {
            return gateRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            return gateRepository.findAll(pageable);
        }
    }

    @Override
    public boolean assignGatekeeperToGate(GateAssignmentRequest request) {
        // Validate gate exists
        Gate gate = gateRepository.findById(request.getGateId())
                .orElseThrow(() -> new ResourceNotFoundException("Gate not found with ID: " + request.getGateId()));

        // Validate gatekeeper exists and is a gatekeeper
        User gatekeeper = userRepository.findById(request.getGatekeeperId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getGatekeeperId()));

        if (gatekeeper.getRole() != UserRole.GATEKEEPER) {
            throw new IllegalArgumentException("User must be a GATEKEEPER to be assigned to a gate");
        }

        // Check if gatekeeper is already assigned to another gate
        if (gatekeeper.getAssignedGate() != null) {
            throw new IllegalArgumentException("Gatekeeper is already assigned to gate: " + gatekeeper.getAssignedGate().getName());
        }

        // Assign gatekeeper to gate
        gatekeeper.setAssignedGate(gate);
        userRepository.save(gatekeeper);

        return true;
    }

    @Override
    public boolean unassignGatekeeperFromGate(UUID gatekeeperId) {
        User gatekeeper = userRepository.findById(gatekeeperId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + gatekeeperId));

        if (gatekeeper.getAssignedGate() == null) {
            throw new IllegalArgumentException("Gatekeeper is not assigned to any gate");
        }

        gatekeeper.setAssignedGate(null);
        userRepository.save(gatekeeper);

        return true;
    }

    @Override
    public List<Gate> getGatesWithAssignedGatekeepers() {
        return gateRepository.findGatesWithAssignedGatekeepers();
    }

    @Override
    public Gate getGateByGatekeeper(UUID gatekeeperId) {
        User gatekeeper = userRepository.findById(gatekeeperId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + gatekeeperId));

        return gatekeeper.getAssignedGate();
    }
} 