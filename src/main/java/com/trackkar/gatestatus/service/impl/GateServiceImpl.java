package com.trackkar.gatestatus.service.impl;

import com.trackkar.gatestatus.entity.Gate;
import com.trackkar.gatestatus.entity.GateStatus;
import com.trackkar.gatestatus.repository.GateRepository;
import com.trackkar.gatestatus.repository.UserRepository;
import com.trackkar.gatestatus.service.interfaces.GateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class GateServiceImpl implements GateService {
    @Autowired
    private GateRepository gateRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Gate createGate(Gate gate) {
        return gateRepository.save(gate);
    }

    @Override
    public Gate updateGateStatus(UUID gateId, GateStatus status, UUID updatedBy) {
        Gate gate = gateRepository.findById(gateId).orElseThrow();
        gate.setStatus(status);
        gate.setLastUpdated(Instant.now());
        gate.setUpdatedBy(userRepository.findById(updatedBy).orElseThrow());
        return gateRepository.save(gate);
    }

    @Override
    public List<Gate> getAllGates() {
        return gateRepository.findAll();
    }

    @Override
    public Gate getGateById(UUID id) {
        return gateRepository.findById(id).orElse(null);
    }
}
