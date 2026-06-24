package com.safehome.backend.domain.service;

import com.safehome.backend.domain.model.PropertyMember;
import com.safehome.backend.domain.repository.PropertyMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PropertyMemberService {

    private final PropertyMemberRepository propertyMemberRepository;

    public PropertyMember create(PropertyMember member) {
        return propertyMemberRepository.save(member);
    }

    public Optional<PropertyMember> findById(UUID id) {
        return propertyMemberRepository.findById(id);
    }

    public List<PropertyMember> findByPropertyId(UUID propertyId) {
        return propertyMemberRepository.findByPropertyId(propertyId);
    }

    public List<PropertyMember> findByUserId(UUID userId) {
        return propertyMemberRepository.findByUserId(userId);
    }

    public void delete(UUID id) {
        propertyMemberRepository.deleteById(id);
    }
}