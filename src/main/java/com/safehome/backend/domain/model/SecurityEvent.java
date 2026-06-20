package com.safehome.backend.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.Map;
@Entity
@Table(name = "security_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityEvent {
    @Column(name = "telemetry_snapshot", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> telemetrySnapshot;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Column(name = "event_type", length = 30)
    private String eventType;

    @Column(name = "severity", length = 10)
    private String severity;

    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "latitude", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 10, scale = 7)
    private BigDecimal longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private SecurityEventStatus status = SecurityEventStatus.PENDING;

    @Column(name = "detected_at")
    private LocalDateTime detectedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @PrePersist
    protected void onCreate() {
        detectedAt = LocalDateTime.now();
    }
}