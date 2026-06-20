package com.safehome.backend.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record SecurityEventRequest(

        @NotNull(message = "El dispositivo es obligatorio")
        UUID deviceId,

        @NotNull(message = "La propiedad es obligatoria")
        UUID propertyId,

        @NotBlank(message = "El tipo de evento es obligatorio")
        String eventType,

        @NotBlank(message = "La severidad es obligatoria")
        String severity,

        @NotBlank(message = "El título es obligatorio")
        String title,

        @NotBlank(message = "La descripción es obligatoria")
        String description,

        @NotNull(message = "La latitud es obligatoria")
        BigDecimal latitude,

        @NotNull(message = "La longitud es obligatoria")
        BigDecimal longitude

) {
}