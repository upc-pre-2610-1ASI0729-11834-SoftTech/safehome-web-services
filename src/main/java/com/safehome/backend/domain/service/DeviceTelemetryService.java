package com.safehome.backend.domain.service;

import com.safehome.backend.domain.model.DeviceTelemetry;
import com.safehome.backend.domain.repository.DeviceTelemetryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceTelemetryService {

    private final DeviceTelemetryRepository deviceTelemetryRepository;

    public DeviceTelemetry create(DeviceTelemetry telemetry) {
        return deviceTelemetryRepository.save(telemetry);
    }

    public Optional<DeviceTelemetry> findById(UUID id) {
        return deviceTelemetryRepository.findById(id);
    }

    public List<DeviceTelemetry> findByDeviceId(UUID deviceId) {
        return deviceTelemetryRepository.findByDeviceId(deviceId);
    }

    public List<DeviceTelemetry> findByDeviceIdOrderByDate(UUID deviceId) {
        return deviceTelemetryRepository.findByDeviceIdOrderByRecordedAtDesc(deviceId);
    }
}