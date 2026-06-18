package com.safehome.backend.interfaces.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class DeviceTelemetryRequest {
    private UUID deviceId;
    private BigDecimal readingValue;
    private String unit;
}