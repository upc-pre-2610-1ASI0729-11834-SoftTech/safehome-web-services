package com.safehome.backend.interfaces.rest;

import com.safehome.backend.domain.model.Property;
import com.safehome.backend.domain.model.User;
import com.safehome.backend.domain.service.PropertyService;
import com.safehome.backend.interfaces.dto.PropertyRequest;
import com.safehome.backend.interfaces.dto.PropertyResponse;
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
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
@Tag(name = "Properties", description = "Gestión de propiedades")
public class PropertyController {

    private final PropertyService propertyService;
    @Operation(summary = "Crear propiedad")
    @PostMapping
    public ResponseEntity<PropertyResponse> create(@Valid @RequestBody PropertyRequest request) {
        Property property = new Property();
        User user = new User();
        user.setId(request.getUserId());
        property.setUser(user);
        property.setName(request.getName());
        property.setAddress(request.getAddress());
        property.setLatitude(request.getLatitude());
        property.setLongitude(request.getLongitude());
        property.setPropertyType(request.getPropertyType());

        Property saved = propertyService.create(property);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @Operation(summary = "Obtener lista de propiedades")
    @GetMapping
    public ResponseEntity<List<PropertyResponse>> findAll() {
        List<PropertyResponse> properties = propertyService.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(properties);
    }

    @Operation(summary = "Listar propiedades")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PropertyResponse>> findByUser(@PathVariable UUID userId) {
        List<PropertyResponse> properties = propertyService.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(properties);
    }
    @Operation(summary = "Obtener propiedad por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> findById(@PathVariable UUID id) {
        return propertyService.findById(id)
                .map(property -> ResponseEntity.ok(toResponse(property)))
                .orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Actualizar propiedad")
    @PutMapping("/{id}")
    public ResponseEntity<PropertyResponse> update(@PathVariable UUID id,
                                                   @Valid @RequestBody PropertyRequest request) {
        return propertyService.findById(id).map(property -> {
            property.setName(request.getName());
            property.setAddress(request.getAddress());
            property.setLatitude(request.getLatitude());
            property.setLongitude(request.getLongitude());
            property.setPropertyType(request.getPropertyType());
            Property updated = propertyService.update(property);
            return ResponseEntity.ok(toResponse(updated));
        }).orElse(ResponseEntity.notFound().build());
    }
    @Operation(summary = "Eliminar propiedad")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        propertyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private PropertyResponse toResponse(Property property) {
        PropertyResponse response = new PropertyResponse();
        response.setId(property.getId());
        response.setUserId(property.getUser() != null ? property.getUser().getId() : null);
        response.setName(property.getName());
        response.setAddress(property.getAddress());
        response.setLatitude(property.getLatitude());
        response.setLongitude(property.getLongitude());
        response.setPropertyType(property.getPropertyType());
        response.setCreatedAt(property.getCreatedAt());
        return response;
    }
}