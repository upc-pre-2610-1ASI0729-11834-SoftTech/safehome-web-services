package com.safehome.backend.interfaces.rest;

import com.safehome.backend.domain.model.Subscription;
import com.safehome.backend.domain.model.User;
import com.safehome.backend.domain.service.SubscriptionService;
import com.safehome.backend.domain.service.UserService;
import com.safehome.backend.interfaces.dto.SubscriptionRequest;
import com.safehome.backend.interfaces.dto.SubscriptionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<SubscriptionResponse> create(@Valid @RequestBody SubscriptionRequest request) {
        User user = userService.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setPlanType(request.getPlanType());
        subscription.setPaymentReference(request.getPaymentReference());

        Subscription saved = subscriptionService.create(subscription);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubscriptionResponse>> findByUser(@PathVariable UUID userId) {
        List<SubscriptionResponse> subscriptions = subscriptionService.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<SubscriptionResponse> findActiveByUser(@PathVariable UUID userId) {
        return subscriptionService.findActiveByUserId(userId)
                .map(s -> ResponseEntity.ok(toResponse(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> findById(@PathVariable UUID id) {
        return subscriptionService.findById(id)
                .map(s -> ResponseEntity.ok(toResponse(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<SubscriptionResponse> cancel(@PathVariable UUID id) {
        return subscriptionService.findById(id).map(s -> {
            s.setStatus("CANCELLED");
            Subscription updated = subscriptionService.update(s);
            return ResponseEntity.ok(toResponse(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    private SubscriptionResponse toResponse(Subscription subscription) {
        SubscriptionResponse response = new SubscriptionResponse();
        response.setId(subscription.getId());
        response.setUserId(subscription.getUser().getId());
        response.setPlanType(subscription.getPlanType());
        response.setStatus(subscription.getStatus());
        response.setStartDate(subscription.getStartDate());
        response.setEndDate(subscription.getEndDate());
        response.setPaymentReference(subscription.getPaymentReference());
        response.setCreatedAt(subscription.getCreatedAt());
        return response;
    }
}