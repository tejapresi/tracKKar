package com.trackkar.gatestatus.repository;

import com.trackkar.gatestatus.entity.Gate;
import com.trackkar.gatestatus.entity.GateStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GateRepository extends JpaRepository<Gate, UUID> {
    
    // Find gates by status
    Page<Gate> findByStatus(GateStatus status, Pageable pageable);
    
    // Find gates by name containing (case-insensitive)
    Page<Gate> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // Find gates that have assigned gatekeepers
    @Query("SELECT DISTINCT g FROM Gate g WHERE EXISTS (SELECT u FROM User u WHERE u.assignedGate = g)")
    List<Gate> findGatesWithAssignedGatekeepers();
}