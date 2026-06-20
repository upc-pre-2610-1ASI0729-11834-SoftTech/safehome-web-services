package com.safehome.backend.domain.service;

import org.springframework.transaction.annotation.Transactional;
import com.safehome.backend.domain.model.Device;
import com.safehome.backend.domain.repository.AlertRepository;
import com.safehome.backend.domain.model.Property;
import com.safehome.backend.domain.model.SecurityEvent;
import com.safehome.backend.domain.repository.DeviceRepository;
import com.safehome.backend.domain.repository.PropertyRepository;
import com.safehome.backend.domain.repository.SecurityEventRepository;
import com.safehome.backend.interfaces.dto.SecurityEventRequest;
import com.safehome.backend.interfaces.dto.SecurityEventResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.safehome.backend.domain.model.SecurityEventStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class SecurityEventService {
    private final AlertService alertService;
    private final SecurityEventRepository securityEventRepository;
    private final DeviceRepository deviceRepository;
    private final PropertyRepository propertyRepository;
    private final AlertRepository alertRepository;

    @Transactional
    public SecurityEventResponse create(SecurityEventRequest request) {

        Device device = deviceRepository.findById(request.deviceId())
                .orElseThrow(() -> new RuntimeException("Device not found"));

        Property property = propertyRepository.findById(request.propertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        SecurityEvent event = SecurityEvent.builder()
                .device(device)
                .property(property)
                .eventType(request.eventType())
                .severity(request.severity())
                .title(request.title())
                .description(request.description())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .status(SecurityEventStatus.PENDING)
                .build();

        SecurityEvent savedEvent = securityEventRepository.save(event);

        if ("CRITICAL".equalsIgnoreCase(savedEvent.getSeverity())) {
            alertService.createFromSecurityEvent(savedEvent);
        }

        return mapToResponse(savedEvent);
    }
    @Transactional(readOnly = true)
    public List<SecurityEventResponse> findAll() {
        return securityEventRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SecurityEventResponse> findByProperty(UUID propertyId) {
        return securityEventRepository.findByPropertyId(propertyId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SecurityEventResponse findById(UUID id) {

        SecurityEvent event = securityEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Security event not found"));

        return mapToResponse(event);
    }

    public SecurityEvent resolve(UUID id) {
        SecurityEvent event = securityEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setStatus(SecurityEventStatus.RESOLVED);
        event.setResolvedAt(LocalDateTime.now());

        return securityEventRepository.save(event);
    }

    public SecurityEvent reactivate(UUID id) {
        SecurityEvent event = securityEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setStatus(SecurityEventStatus.REACTIVATED);
        event.setResolvedAt(null);

        return securityEventRepository.save(event);
    }

    public SecurityEvent attend(UUID id) {
        SecurityEvent event = securityEventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setStatus(SecurityEventStatus.ATTENDED);

        return securityEventRepository.save(event);
    }

    @Transactional
    public void delete(UUID id) {
        alertRepository.deleteBySecurityEventId(id);
        securityEventRepository.deleteById(id);
    }

    private SecurityEventResponse mapToResponse(SecurityEvent event) {
        return new SecurityEventResponse(
                event.getId(),
                event.getDevice().getId(),
                event.getProperty().getId(),
                event.getEventType(),
                event.getSeverity(),
                event.getTitle(),
                event.getDescription(),
                event.getStatus(),
                event.getDevice().getName(),
                event.getDevice().getLocationArea(),
                event.getDetectedAt()
        );
    }
}