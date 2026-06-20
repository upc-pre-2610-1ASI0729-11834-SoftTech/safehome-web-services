package com.safehome.backend.domain.repository;

import com.safehome.backend.domain.model.SecurityEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SecurityEventRepository
        extends JpaRepository<SecurityEvent, UUID> {
        List<SecurityEvent> findByPropertyId(UUID propertyId);
}
