package com.safehome.backend.domain.service;

import com.safehome.backend.domain.model.Alert;
import com.safehome.backend.domain.model.Notification;
import com.safehome.backend.domain.repository.AlertRepository;
import com.safehome.backend.domain.repository.NotificationRepository;
import com.safehome.backend.interfaces.dto.NotificationRequest;
import com.safehome.backend.interfaces.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final AlertRepository alertRepository;

    public NotificationResponse create(NotificationRequest request) {

        Alert alert = alertRepository.findById(request.alertId())
                .orElseThrow(() -> new RuntimeException("Alert not found"));

        Notification notification = Notification.builder()
                .alert(alert)
                .channel(request.channel())
                .recipient(request.recipient())
                .providerMessageId(UUID.randomUUID().toString())
                .deliveryStatus("PENDING")
                .sentAt(LocalDateTime.now())
                .build();

        Notification saved = notificationRepository.save(notification);

        return mapToResponse(saved);
    }

    public List<NotificationResponse> findAll() {

        return notificationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public NotificationResponse findById(UUID id) {

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        return mapToResponse(notification);
    }

    public NotificationResponse markAsDelivered(UUID id) {

        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setDeliveryStatus("DELIVERED");

        Notification updated = notificationRepository.save(notification);

        return mapToResponse(updated);
    }

    private NotificationResponse mapToResponse(Notification notification) {

        return new NotificationResponse(
                notification.getId(),
                notification.getAlert().getId(),
                notification.getChannel(),
                notification.getRecipient(),
                notification.getProviderMessageId(),
                notification.getDeliveryStatus(),
                notification.getSentAt()
        );
    }
}