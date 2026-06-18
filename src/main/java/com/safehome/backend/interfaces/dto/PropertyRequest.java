package com.safehome.backend.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PropertyRequest {

    @NotNull(message = "El usuario es obligatorio")
    private UUID userId;

    @NotBlank(message = "El nombre de la propiedad es obligatorio")
    private String name;

    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    @NotNull(message = "La latitud es obligatoria")
    private BigDecimal latitude;

    @NotNull(message = "La longitud es obligatoria")
    private BigDecimal longitude;

    @NotBlank(message = "El tipo de propiedad es obligatorio")
    private String propertyType;
}