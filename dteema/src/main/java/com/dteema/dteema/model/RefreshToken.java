package com.dteema.dteema.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // Optional: Track device/IP for security
    @Column(name = "device_info")
    private String deviceInfo;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    // Check if token is expired
    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }
}