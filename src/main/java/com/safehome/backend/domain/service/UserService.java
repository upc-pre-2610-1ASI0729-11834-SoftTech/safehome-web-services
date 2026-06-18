package com.safehome.backend.domain.service;

import com.safehome.backend.domain.model.Property;
import com.safehome.backend.domain.repository.PropertyRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import com.safehome.backend.domain.model.User;
import com.safehome.backend.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.safehome.backend.domain.model.UserSettings;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSettingsService userSettingsService;
    private final PropertyRepository propertyRepository;

    @Transactional
    public User register(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email ya registrado");
        }

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        User savedUser = userRepository.save(user);
        Property defaultProperty = new Property();
        defaultProperty.setUser(savedUser);
        defaultProperty.setName("Mi Hogar");
        defaultProperty.setAddress("Lima, Perú");
        defaultProperty.setLatitude(BigDecimal.valueOf(-12.0464));
        defaultProperty.setLongitude(BigDecimal.valueOf(-77.0428));
        defaultProperty.setPropertyType("HOUSE");

        propertyRepository.save(defaultProperty);

        UserSettings settings = new UserSettings();
        settings.setUser(savedUser);

        settings.setNotificationsEnabled(true);
        settings.setPushEnabled(true);
        settings.setSmsEnabled(false);
        settings.setEmailEnabled(true);

        settings.setAlertSensitivity("MEDIUM");
        settings.setHomeName("Mi Hogar");

        settings.setEmailSummary(true);
        settings.setAutoArm(false);
        settings.setDarkMode(false);

        settings.setTwoFactor(false);
        settings.setAutoLogout(true);
        settings.setLoginAlerts(true);

        settings.setLanguage("es");

        userSettingsService.create(settings);

        return savedUser;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User update(User user) {
        return userRepository.save(user);
    }
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}