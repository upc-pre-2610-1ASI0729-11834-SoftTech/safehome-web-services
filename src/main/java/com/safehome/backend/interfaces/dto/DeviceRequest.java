package com.safehome.backend.interfaces.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class DeviceRequest {

    @NotNull(message = "La propiedad es obligatoria")
    private UUID propertyId;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El tipo de dispositivo es obligatorio")
    private String deviceType;

    @NotBlank(message = "La ubicación es obligatoria")
    private String locationArea;

    @NotBlank(message = "El estado es obligatorio")
    private String status;

    private String qrCode;

    private Map<String, Object> attributes;

    @Min(value = 0, message = "La batería no puede ser menor a 0")
    @Max(value = 100, message = "La batería no puede ser mayor a 100")
    private Integer battery;

    private String description;

    @Min(value = 0, message = "La intensidad de señal no puede ser negativa")
    @Max(value = 100, message = "La intensidad de señal no puede ser mayor a 100")
    private Integer signalStrength;
}