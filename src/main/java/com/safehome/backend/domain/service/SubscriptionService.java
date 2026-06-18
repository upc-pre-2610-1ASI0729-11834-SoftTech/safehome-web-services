package com.safehome.backend.domain.service;

import com.safehome.backend.domain.model.Subscription;
import com.safehome.backend.domain.repository.SubscriptionRepository;
import com.safehome.backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public Subscription create(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public Optional<Subscription> findById(UUID id) {
        return subscriptionRepository.findById(id);
    }

    public List<Subscription> findByUserId(UUID userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    public Optional<Subscription> findActiveByUserId(UUID userId) {
        return subscriptionRepository.findByUserIdAndStatus(userId, "ACTIVE");
    }

    public Subscription update(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public void delete(UUID id) {
        subscriptionRepository.deleteById(id);
    }
}