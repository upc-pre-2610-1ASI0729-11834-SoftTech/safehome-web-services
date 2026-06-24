package com.safehome.backend.domain.service;

import com.safehome.backend.domain.model.Property;
import com.safehome.backend.domain.model.User;
import com.safehome.backend.domain.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public Property create(Property property) {
        return propertyRepository.save(property);
    }

    public Optional<Property> findById(UUID id) {
        return propertyRepository.findById(id);
    }

    public List<Property> findByUserId(UUID userId) {
        return propertyRepository.findByUserId(userId);
    }

    public List<Property> findAll() {
        return propertyRepository.findAll();
    }

    public Property update(Property property) {
        return propertyRepository.save(property);
    }

    public void delete(UUID id) {
        propertyRepository.deleteById(id);
    }
}