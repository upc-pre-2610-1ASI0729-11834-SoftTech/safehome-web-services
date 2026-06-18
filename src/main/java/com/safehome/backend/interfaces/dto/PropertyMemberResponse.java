package com.safehome.backend.interfaces.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PropertyMemberResponse {
    private UUID id;
    private UUID userId;
    private UUID propertyId;
    private String role;
    private LocalDateTime joinedAt;
}