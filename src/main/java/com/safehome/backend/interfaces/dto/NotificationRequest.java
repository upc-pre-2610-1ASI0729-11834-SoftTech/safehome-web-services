package com.safehome.backend.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NotificationRequest(

        @NotNull(message = "La alerta es obligatoria")
        UUID alertId,

        @NotBlank(message = "El canal es obligatorio")
        String channel,

        @NotBlank(message = "El destinatario es obligatorio")
        String recipient

) {
}