package com.safehome.backend.domain.service;

import com.safehome.backend.infrastructure.tuya.TuyaCloudClient;
import com.safehome.backend.interfaces.dto.SecurityEventRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TuyaMotionPollingService {

    private final TuyaCloudClient tuyaCloudClient;
    private final SecurityEventService securityEventService;

    @Value("${tuya.polling.enabled:false}")
    private boolean pollingEnabled;

    @Value("${tuya.device-id:}")
    private String tuyaDeviceId;

    @Value("${tuya.motion-code:pir_state}")
    private String motionCode;

    @Value("${tuya.motion-value:pir}")
    private String motionValue;

    @Value("${safehome.tuya.device-id:}")
    private String safeHomeDeviceId;

    @Value("${safehome.tuya.property-id:}")
    private String safeHomePropertyId;

    @Value("${safehome.tuya.latitude:-12.0464}")
    private BigDecimal latitude;

    @Value("${safehome.tuya.longitude:-77.0428}")
    private BigDecimal longitude;

    @Value("${tuya.motion.cooldown-ms:120000}")
    private long cooldownMs;

    private long lastAlertTime = 0;
    private boolean previousMotionDetected = false;

    @PostConstruct
    public void init() {
        if (pollingEnabled) {
            log.info("Tuya motion polling enabled for device {}", tuyaDeviceId);
        } else {
            log.info("Tuya motion polling disabled.");
        }
    }

    @Scheduled(fixedDelayString = "${tuya.polling.delay-ms:1000}")
    public void checkMotionSensor() {
        if (!pollingEnabled) {
            return;
        }

        if (isBlank(tuyaDeviceId) || isBlank(safeHomeDeviceId) || isBlank(safeHomePropertyId)) {
            log.warn("Tuya polling is enabled, but required IDs are missing.");
            return;
        }

        try {
            List<TuyaCloudClient.TuyaDeviceProperty> properties =
                    tuyaCloudClient.getDeviceProperties(tuyaDeviceId);

            boolean motionDetected = properties.stream()
                    .anyMatch(property ->
                            motionCode.equalsIgnoreCase(property.code())
                                    && motionValue.equalsIgnoreCase(property.value())
                    );

            if (!motionDetected) {
                previousMotionDetected = false;
                return;
            }

            if (previousMotionDetected) {
                return;
            }

            previousMotionDetected = true;

            long now = Instant.now().toEpochMilli();

            if (now - lastAlertTime < cooldownMs) {
                log.info("Tuya motion detected, but cooldown is active.");
                return;
            }

            lastAlertTime = now;

            log.info("Tuya motion detected. Creating SafeHome security event.");

            SecurityEventRequest request = new SecurityEventRequest(
                    UUID.fromString(safeHomeDeviceId),
                    UUID.fromString(safeHomePropertyId),
                    "MOTION_DETECTED",
                    "CRITICAL",
                    "Movimiento detectado por sensor físico",
                    "El sensor PIR conectado mediante Tuya Smart detectó movimiento en la vivienda.",
                    latitude,
                    longitude
            );

            securityEventService.create(request);

            log.info("SafeHome security event created from Tuya motion sensor.");

        } catch (Exception e) {
            log.error("Could not process Tuya motion sensor state.", e);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}