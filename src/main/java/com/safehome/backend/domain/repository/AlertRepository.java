package com.safehome.backend.domain.repository;

import com.safehome.backend.domain.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlertRepository
        extends JpaRepository<Alert, UUID> {
        void deleteBySecurityEventId(UUID securityEventId);
}
