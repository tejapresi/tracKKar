package com.trackkar.gatestatus.repository;

import com.trackkar.gatestatus.entity.Gate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GateRepository extends JpaRepository<Gate, UUID> {
}