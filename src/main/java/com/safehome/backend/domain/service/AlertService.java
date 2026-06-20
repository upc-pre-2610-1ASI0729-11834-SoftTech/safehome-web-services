package com.safehome.backend.domain.service;

import org.springframework.transaction.annotation.Transactional;
import com.safehome.backend.domain.model.Alert;
import com.safehome.backend.domain.model.AlertStatus;
import com.safehome.backend.domain.model.SecurityEvent;
import com.safehome.backend.domain.model.User;
import com.safehome.backend.domain.repository.AlertRepository;
import com.safehome.backend.domain.repository.SecurityEventRepository;
import com.safehome.backend.domain.repository.UserRepository;
import com.safehome.backend.infrastructure.EmailService;
import com.safehome.backend.interfaces.dto.AlertRequest;
import com.safehome.backend.interfaces.dto.AlertResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlertService {
    private final EmailService emailService;
    private final AlertRepository alertRepository;
    private final UserRepository userRepository;
    private final SecurityEventRepository securityEventRepository;

    public AlertResponse create(AlertRequest request) {

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        SecurityEvent event = securityEventRepository.findById(request.securityEventId())
                .orElseThrow(() -> new RuntimeException("Security event not found"));

        Alert alert = Alert.builder()
                .user(user)
                .securityEvent(event)
                .priority(request.priority())
                .message(request.message())
                .status(AlertStatus.PENDING)
                .sentAt(LocalDateTime.now())
                .build();

        Alert saved = alertRepository.save(alert);

        return mapToResponse(saved);
    }

    public List<AlertResponse> findAll() {

        return alertRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AlertResponse findById(UUID id) {

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found"));

        return mapToResponse(alert);
    }

    public AlertResponse markAsRead(UUID id) {

        Alert alert = alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found"));

        alert.setStatus(AlertStatus.READ);
        alert.setReadAt(LocalDateTime.now());

        Alert updated = alertRepository.save(alert);

        return mapToResponse(updated);
    }

    @Transactional
    public void createFromSecurityEvent(SecurityEvent event) {

        User user = event.getProperty().getUser();
        Alert alert = Alert.builder()
                .user(user)
                .securityEvent(event)
                .priority(event.getSeverity())
                .message(event.getTitle())
                .status(AlertStatus.PENDING)
                .build();

        alertRepository.save(alert);
        if ("CRITICAL".equalsIgnoreCase(event.getSeverity())) {
            emailService.sendAlertEmail(
                    user.getEmail(),
                    "ALERTA CRÍTICA SAFEHOME",
                    event.getTitle() + "\n\n" + event.getDescription()
            );
        }
    }
    private AlertResponse mapToResponse(Alert alert) {

        return new AlertResponse(
                alert.getId(),
                alert.getUser().getId(),
                alert.getSecurityEvent().getId(),
                alert.getPriority(),
                alert.getMessage(),
                alert.getStatus().name(),
                alert.getSentAt()
        );
    }
}