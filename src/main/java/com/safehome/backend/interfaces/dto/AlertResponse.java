package com.safehome.backend.interfaces.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AlertResponse(
        UUID id,
        UUID userId,
        UUID securityEventId,
        String priority,
        String message,
        String status,
        LocalDateTime sentAt
) {
}