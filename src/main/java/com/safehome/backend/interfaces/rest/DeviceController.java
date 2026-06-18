package com.safehome.backend.interfaces.rest;

import com.safehome.backend.domain.model.Device;
import com.safehome.backend.domain.model.Property;
import com.safehome.backend.domain.service.DeviceService;
import com.safehome.backend.domain.service.PropertyService;
import com.safehome.backend.interfaces.dto.DeviceRequest;
import com.safehome.backend.interfaces.dto.DeviceResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/devices")
@RequiredArgsConstructor
@Tag(name = "Devices", description = "Gestión de dispositivos IoT")
public class DeviceController {

    private final DeviceService deviceService;
    private final PropertyService propertyService;

    @PostMapping
    public ResponseEntity<DeviceResponse> create(@Valid @RequestBody DeviceRequest request) {
        Property property = propertyService.findById(request.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));

        Device device = new Device();
        device.setProperty(property);
        device.setName(request.getName());
        device.setDeviceType(request.getDeviceType());
        device.setLocationArea(request.getLocationArea());
        device.setStatus(request.getStatus() != null ? request.getStatus() : "ACTIVE");
        device.setQrCode(request.getQrCode());
        device.setAttributes(request.getAttributes());
        device.setBattery(request.getBattery() != null ? request.getBattery() : 100);
        device.setDescription(request.getDescription());
        device.setSignalStrength(request.getSignalStrength() != null ? request.getSignalStrength() : 100);

        Device saved = deviceService.create(device);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<DeviceResponse>> findAll() {
        List<DeviceResponse> devices = deviceService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<DeviceResponse>> findByProperty(@PathVariable UUID propertyId) {
        List<DeviceResponse> devices = deviceService.findByPropertyId(propertyId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponse> findById(@PathVariable UUID id) {
        return deviceService.findById(id)
                .map(device -> ResponseEntity.ok(toResponse(device)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponse> update(@PathVariable UUID id,
                                                 @Valid @RequestBody DeviceRequest request) {
        return deviceService.findById(id).map(device -> {
            device.setName(request.getName());
            device.setStatus(request.getStatus());
            device.setLocationArea(request.getLocationArea());
            device.setBattery(request.getBattery());
            device.setAttributes(request.getAttributes());
            device.setSignalStrength(request.getSignalStrength());
            Device updated = deviceService.update(device);
            return ResponseEntity.ok(toResponse(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deviceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private DeviceResponse toResponse(Device device) {
        DeviceResponse response = new DeviceResponse();
        response.setId(device.getId());
        response.setPropertyId(device.getProperty() != null ? device.getProperty().getId() : null);
        response.setName(device.getName());
        response.setDeviceType(device.getDeviceType());
        response.setLocationArea(device.getLocationArea());
        response.setStatus(device.getStatus());
        response.setQrCode(device.getQrCode());
        response.setAttributes(device.getAttributes());
        response.setBattery(device.getBattery());
        response.setLastSeenAt(device.getLastSeenAt());
        response.setDescription(device.getDescription());
        response.setSignalStrength(device.getSignalStrength());
        response.setRegisteredAt(device.getRegisteredAt());
        return response;
    }
}