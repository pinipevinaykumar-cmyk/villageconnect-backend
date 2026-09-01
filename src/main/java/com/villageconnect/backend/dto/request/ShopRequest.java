package com.villageconnect.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ShopRequest {

    @NotBlank(message = "Shop name is required")
    private String name;

    private String ownerName;

    @NotBlank(message = "Phone is required")
    private String phone;

    private String whatsapp;

    private String description;

    private String address;

    @NotBlank(message = "Village is required")
    private String village;

    private String district;

    private String state;

    private Double latitude;
    private Double longitude;

    private Long categoryId;

    private String openTime;
    private String closeTime;

    private Boolean is24Hours;
    private Boolean isDeliveryAvailable;

    private String imageUrl;
}