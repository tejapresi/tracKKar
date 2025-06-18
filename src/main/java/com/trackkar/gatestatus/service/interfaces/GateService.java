package com.trackkar.gatestatus.service.interfaces;

import com.trackkar.gatestatus.dto.GateAssignmentRequest;
import com.trackkar.gatestatus.dto.GateCreateRequest;
import com.trackkar.gatestatus.entity.Gate;
import com.trackkar.gatestatus.entity.GateStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface GateService {
    Gate createGate(GateCreateRequest request);
    Gate updateGateStatus(UUID gateId, GateStatus status, UUID updatedBy);
    Gate getGateById(UUID id);
    boolean deleteGate(UUID id);
    Page<Gate> getGatesWithPagination(Pageable pageable, String status, String search);
    
    // Gate assignment methods
    boolean assignGatekeeperToGate(GateAssignmentRequest request);
    boolean unassignGatekeeperFromGate(UUID gatekeeperId);
    List<Gate> getGatesWithAssignedGatekeepers();
    Gate getGateByGatekeeper(UUID gatekeeperId);
}
