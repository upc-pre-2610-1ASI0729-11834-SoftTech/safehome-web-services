package com.safehome.backend.domain.repository;

import com.safehome.backend.domain.model.DeviceTelemetry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface DeviceTelemetryRepository extends JpaRepository<DeviceTelemetry, UUID> {
    List<DeviceTelemetry> findByDeviceId(UUID deviceId);
    List<DeviceTelemetry> findByDeviceIdOrderByRecordedAtDesc(UUID deviceId);
}