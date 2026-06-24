package com.safehome.backend.domain.repository;

import com.safehome.backend.domain.model.PropertyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface PropertyMemberRepository extends JpaRepository<PropertyMember, UUID> {
    List<PropertyMember> findByPropertyId(UUID propertyId);
    List<PropertyMember> findByUserId(UUID userId);
}