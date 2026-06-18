package com.safehome.backend.interfaces.rest;

import com.safehome.backend.domain.model.Device;
import com.safehome.backend.domain.service.DeviceService;
import com.safehome.backend.interfaces.dto.ZoneResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/zones")
@RequiredArgsConstructor
public class ZoneController {

    private final DeviceService deviceService;

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<ZoneResponse>> findByProperty(@PathVariable UUID propertyId) {
        List<Device> devices = deviceService.findByPropertyId(propertyId);

        Map<String, List<Device>> grouped = devices.stream()
                .filter(d -> d.getLocationArea() != null)
                .collect(Collectors.groupingBy(Device::getLocationArea));

        List<ZoneResponse> zones = grouped.entrySet().stream().map(entry -> {
            String zone = entry.getKey();
            List<Device> zoneDevices = entry.getValue();

            ZoneResponse response = new ZoneResponse();
            response.setName(zone);
            response.setDeviceCount((long) zoneDevices.size());

            boolean hasCritical = zoneDevices.stream()
                    .anyMatch(d -> "DISCONNECTED".equals(d.getStatus()));
            boolean hasWarning = zoneDevices.stream()
                    .anyMatch(d -> d.getBattery() != null && d.getBattery() < 20);

            response.setStatus(hasCritical ? "critical" : hasWarning ? "warning" : "safe");

            double avgSignal = zoneDevices.stream()
                    .filter(d -> d.getSignalStrength() != null)
                    .mapToInt(Device::getSignalStrength)
                    .average()
                    .orElse(100);

            response.setSignalStrength((int) avgSignal);
            return response;
        }).toList();

        return ResponseEntity.ok(zones);
    }
}