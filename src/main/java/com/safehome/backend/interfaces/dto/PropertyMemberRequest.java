package com.safehome.backend.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;
@Data
public class PropertyMemberRequest {

    @NotNull(message = "El usuario es obligatorio")
    private UUID userId;

    @NotNull(message = "La propiedad es obligatoria")
    private UUID propertyId;

    @NotBlank(message = "El rol es obligatorio")
    private String role;
}