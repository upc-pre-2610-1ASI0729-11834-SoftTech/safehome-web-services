package com.safehome.backend.interfaces.rest;

import com.safehome.backend.domain.service.AlertService;
import com.safehome.backend.interfaces.dto.AlertRequest;
import com.safehome.backend.interfaces.dto.AlertResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
@Tag(name = "Alerts", description = "Gestión de alertas")
public class AlertController {

    private final AlertService alertService;

    @PostMapping
    public AlertResponse create(
            @Valid @RequestBody AlertRequest request) {

        return alertService.create(request);
    }

    @GetMapping
    public List<AlertResponse> findAll() {

        return alertService.findAll();
    }

    @GetMapping("/{id}")
    public AlertResponse findById(
            @PathVariable UUID id) {

        return alertService.findById(id);
    }

    @PatchMapping("/{id}/read")
    public AlertResponse markAsRead(
            @PathVariable UUID id) {

        return alertService.markAsRead(id);
    }
}