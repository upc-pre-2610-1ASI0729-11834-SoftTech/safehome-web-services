package com.safehome.backend.interfaces.dto;
import java.time.LocalDateTime;
import java.util.UUID;
import com.safehome.backend.domain.model.SecurityEventStatus;
public record SecurityEventResponse(
        UUID id,
        UUID deviceId,
        UUID propertyId,
        String eventType,
        String severity,
        String title,
        String description,
        SecurityEventStatus status,
        String deviceName,
        String locationArea,
        LocalDateTime detectedAt

) {
}