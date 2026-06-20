package com.safehome.backend.interfaces.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        UUID alertId,
        String channel,
        String recipient,
        String providerMessageId,
        String deliveryStatus,
        LocalDateTime sentAt
) {
}