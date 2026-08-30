package com.villageconnect.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shops")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(nullable = false)
    private String phone;

    private String whatsapp;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String address;

    private String village;

    private String district;

    private String state;

    private Double latitude;

    private Double longitude;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private User merchant;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_status")
    private ShopStatus currentStatus = ShopStatus.CLOSED;

    @Column(name = "is_manually_set")
    private Boolean isManuallySet = false;

    @Column(name = "open_time")
    private String openTime;

    @Column(name = "close_time")
    private String closeTime;

    @Column(name = "is_24_hours")
    private Boolean is24Hours = false;

    @Column(name = "is_delivery_available")
    private Boolean isDeliveryAvailable = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum ShopStatus {
        OPEN, CLOSED, TEMPORARILY_CLOSED
    }
}