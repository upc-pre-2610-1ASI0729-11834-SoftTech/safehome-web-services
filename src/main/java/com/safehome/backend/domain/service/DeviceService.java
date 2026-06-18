package com.safehome.backend.domain.service;

import com.safehome.backend.domain.model.Device;
import com.safehome.backend.domain.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public Device create(Device device) {
        return deviceRepository.save(device);
    }

    public Optional<Device> findById(UUID id) {
        return deviceRepository.findById(id);
    }

    public List<Device> findByPropertyId(UUID propertyId) {
        return deviceRepository.findByPropertyId(propertyId);
    }

    public List<Device> findByPropertyIdAndStatus(UUID propertyId, String status) {
        return deviceRepository.findByPropertyIdAndStatus(propertyId, status);
    }

    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    public Device update(Device device) {
        return deviceRepository.save(device);
    }

    public void delete(UUID id) {
        deviceRepository.deleteById(id);
    }
}