package com.safehome.backend.interfaces.rest;

import com.safehome.backend.domain.service.NotificationService;
import com.safehome.backend.interfaces.dto.NotificationRequest;
import com.safehome.backend.interfaces.dto.NotificationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public NotificationResponse create(
            @Valid @RequestBody NotificationRequest request) {

        return notificationService.create(request);
    }

    @GetMapping
    public List<NotificationResponse> findAll() {

        return notificationService.findAll();
    }

    @GetMapping("/{id}")
    public NotificationResponse findById(
            @PathVariable UUID id) {

        return notificationService.findById(id);
    }

    @PatchMapping("/{id}/delivered")
    public NotificationResponse markAsDelivered(
            @PathVariable UUID id) {

        return notificationService.markAsDelivered(id);
    }
}