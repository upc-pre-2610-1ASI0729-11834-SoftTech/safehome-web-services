package com.safehome.backend.interfaces.rest;

import com.safehome.backend.domain.model.Property;
import com.safehome.backend.domain.model.PropertyMember;
import com.safehome.backend.domain.model.User;
import com.safehome.backend.domain.service.PropertyMemberService;
import com.safehome.backend.domain.service.PropertyService;
import com.safehome.backend.domain.service.UserService;
import com.safehome.backend.interfaces.dto.PropertyMemberRequest;
import com.safehome.backend.interfaces.dto.PropertyMemberResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/property-members")
@RequiredArgsConstructor
public class PropertyMemberController {

    private final PropertyMemberService propertyMemberService;
    private final UserService userService;
    private final PropertyService propertyService;

    @PostMapping
    public ResponseEntity<PropertyMemberResponse> create(@Valid @RequestBody PropertyMemberRequest request) {
        User user = userService.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Property property = propertyService.findById(request.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        PropertyMember member = new PropertyMember();
        member.setUser(user);
        member.setProperty(property);
        member.setRole(request.getRole());

        PropertyMember saved = propertyMemberService.create(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<PropertyMemberResponse>> findByProperty(@PathVariable UUID propertyId) {
        List<PropertyMemberResponse> members = propertyMemberService.findByPropertyId(propertyId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(members);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PropertyMemberResponse>> findByUser(@PathVariable UUID userId) {
        List<PropertyMemberResponse> members = propertyMemberService.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(members);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        propertyMemberService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private PropertyMemberResponse toResponse(PropertyMember member) {
        PropertyMemberResponse response = new PropertyMemberResponse();
        response.setId(member.getId());
        response.setUserId(member.getUser().getId());
        response.setPropertyId(member.getProperty().getId());
        response.setRole(member.getRole());
        response.setJoinedAt(member.getJoinedAt());
        return response;
    }
}