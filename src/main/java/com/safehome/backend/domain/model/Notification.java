package com.safehome.backend.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alert_id")
    private Alert alert;

    private String channel;

    private String recipient;

    @Column(name = "provider_message_id")
    private String providerMessageId;

    @Column(name = "delivery_status")
    private String deliveryStatus;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @PrePersist
    public void onCreate() {

        sentAt = LocalDateTime.now();

        if (deliveryStatus == null) {
            deliveryStatus = "PENDING";
        }
    }
}