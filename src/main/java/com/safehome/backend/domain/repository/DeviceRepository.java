package com.safehome.backend.domain.repository;

import com.safehome.backend.domain.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {
    List<Device> findByPropertyId(UUID propertyId);
    List<Device> findByPropertyIdAndStatus(UUID propertyId, String status);
    List<Device> findByPropertyIdAndDeviceType(UUID propertyId, String deviceType);
}