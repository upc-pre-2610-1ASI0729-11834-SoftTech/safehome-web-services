package com.safehome.backend.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AlertRequest(

        @NotNull(message = "El usuario es obligatorio")
        UUID userId,

        @NotNull(message = "El evento de seguridad es obligatorio")
        UUID securityEventId,

        @NotBlank(message = "La prioridad es obligatoria")
        String priority,

        @NotBlank(message = "El mensaje es obligatorio")
        String message

) {
}