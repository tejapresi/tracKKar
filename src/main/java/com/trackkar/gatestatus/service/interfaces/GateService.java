package com.trackkar.gatestatus.service.interfaces;

import com.trackkar.gatestatus.entity.Gate;
import com.trackkar.gatestatus.entity.GateStatus;

import java.util.List;
import java.util.UUID;

public interface GateService {
    Gate createGate(Gate gate);
    Gate updateGateStatus(UUID gateId, GateStatus status, UUID updatedBy);
    List<Gate> getAllGates();
    Gate getGateById(UUID id);
}
