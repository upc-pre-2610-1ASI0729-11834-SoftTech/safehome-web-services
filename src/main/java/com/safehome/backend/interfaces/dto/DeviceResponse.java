package com.safehome.backend.interfaces.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class DeviceResponse {
    private UUID id;
    private UUID propertyId;
    private String name;
    private String deviceType;
    private String locationArea;
    private String status;
    private String qrCode;
    private Map<String, Object> attributes;
    private Integer battery;
    private LocalDateTime lastSeenAt;
    private String description;
    private Integer signalStrength;
    private LocalDateTime registeredAt;
}