package com.safehome.backend.interfaces.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DeviceTelemetryResponse {
    private UUID id;
    private UUID deviceId;
    private BigDecimal readingValue;
    private String unit;
    private LocalDateTime recordedAt;
}