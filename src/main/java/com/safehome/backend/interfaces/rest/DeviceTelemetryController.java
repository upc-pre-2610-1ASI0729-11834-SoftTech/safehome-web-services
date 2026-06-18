package com.safehome.backend.interfaces.rest;

import com.safehome.backend.domain.model.Device;
import com.safehome.backend.domain.model.DeviceTelemetry;
import com.safehome.backend.domain.service.DeviceService;
import com.safehome.backend.domain.service.DeviceTelemetryService;
import com.safehome.backend.interfaces.dto.DeviceTelemetryRequest;
import com.safehome.backend.interfaces.dto.DeviceTelemetryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/device-telemetry")
@RequiredArgsConstructor
public class DeviceTelemetryController {

    private final DeviceTelemetryService deviceTelemetryService;
    private final DeviceService deviceService;

    @PostMapping
    public ResponseEntity<DeviceTelemetryResponse> create(@Valid @RequestBody DeviceTelemetryRequest request) {
        Device device = deviceService.findById(request.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Device not found"));

        DeviceTelemetry telemetry = new DeviceTelemetry();
        telemetry.setDevice(device);
        telemetry.setReadingValue(request.getReadingValue());
        telemetry.setUnit(request.getUnit());

        DeviceTelemetry saved = deviceTelemetryService.create(telemetry);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<DeviceTelemetryResponse>> findByDevice(@PathVariable UUID deviceId) {
        List<DeviceTelemetryResponse> telemetry = deviceTelemetryService.findByDeviceIdOrderByDate(deviceId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(telemetry);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceTelemetryResponse> findById(@PathVariable UUID id) {
        return deviceTelemetryService.findById(id)
                .map(t -> ResponseEntity.ok(toResponse(t)))
                .orElse(ResponseEntity.notFound().build());
    }

    private DeviceTelemetryResponse toResponse(DeviceTelemetry telemetry) {
        DeviceTelemetryResponse response = new DeviceTelemetryResponse();
        response.setId(telemetry.getId());
        response.setDeviceId(telemetry.getDevice().getId());
        response.setReadingValue(telemetry.getReadingValue());
        response.setUnit(telemetry.getUnit());
        response.setRecordedAt(telemetry.getRecordedAt());
        return response;
    }
}