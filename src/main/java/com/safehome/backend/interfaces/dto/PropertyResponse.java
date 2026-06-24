package com.safehome.backend.interfaces.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PropertyResponse {
    private UUID id;
    private UUID userId;
    private String name;
    private String address;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String propertyType;
    private LocalDateTime createdAt;
}