package com.safehome.backend.interfaces.rest;

import com.safehome.backend.domain.service.SecurityEventService;
import com.safehome.backend.interfaces.dto.SecurityEventRequest;
import com.safehome.backend.interfaces.dto.SecurityEventResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/security-events")
@RequiredArgsConstructor
@Tag(name = "Security Events", description = "Gestión de eventos de seguridad")
public class SecurityEventController {

    private final SecurityEventService securityEventService;

    @Operation(summary = "Registrar evento de seguridad")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SecurityEventResponse create(
            @Valid @RequestBody SecurityEventRequest request) {

        return securityEventService.create(request);
    }

    @Operation(summary = "Listar eventos de seguridad")
    @GetMapping
    public List<SecurityEventResponse> findAll() {
        return securityEventService.findAll();
    }

    @Operation(summary = "Listar eventos de seguridad por propiedad")
    @GetMapping("/property/{propertyId}")
    public List<SecurityEventResponse> findByProperty(@PathVariable UUID propertyId) {
        return securityEventService.findByProperty(propertyId);
    }

    @Operation(summary = "Obtener evento de seguridad por ID")
    @GetMapping("/{id}")
    public SecurityEventResponse findById(@PathVariable UUID id) {
        return securityEventService.findById(id);
    }

    @Operation(summary = "Marcar evento de seguridad como atendido")
    @PatchMapping("/{id}/attend")
    public SecurityEventResponse attend(@PathVariable UUID id) {
        securityEventService.attend(id);
        return securityEventService.findById(id);
    }

    @Operation(summary = "Marcar evento de seguridad como resuelto")
    @PatchMapping("/{id}/resolve")
    public SecurityEventResponse resolve(@PathVariable UUID id) {
        securityEventService.resolve(id);
        return securityEventService.findById(id);
    }

    @Operation(summary = "Reactivar evento de seguridad")
    @PatchMapping("/{id}/reactivate")
    public SecurityEventResponse reactivate(@PathVariable UUID id) {
        securityEventService.reactivate(id);
        return securityEventService.findById(id);
    }

    @Operation(summary = "Eliminar evento de seguridad")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        securityEventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}