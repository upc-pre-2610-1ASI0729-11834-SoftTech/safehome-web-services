package com.safehome.backend.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "device_type", length = 20)
    private String deviceType;

    @Column(name = "location_area", length = 80)
    private String locationArea;

    @Column(name = "status", length = 20)
    private String status = "ACTIVE";

    @Column(name = "qr_code", length = 255)
    private String qrCode;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attributes", columnDefinition = "json")
    private Map<String, Object> attributes;

    @Column(name = "battery")
    private Integer battery = 100;

    @Column(name = "last_seen_at")
    private LocalDateTime lastSeenAt;

    @Column(name = "description")
    private String description;

    @Column(name = "signal_strength")
    private Integer signalStrength = 100;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        registeredAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}