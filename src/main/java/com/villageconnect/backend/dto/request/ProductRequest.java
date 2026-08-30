package com.villageconnect.backend.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private String unit;
    private Boolean isAvailable;
}