package com.safehome.backend.interfaces.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SubscriptionResponse {
    private UUID id;
    private UUID userId;
    private String planType;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String paymentReference;
    private LocalDateTime createdAt;
}