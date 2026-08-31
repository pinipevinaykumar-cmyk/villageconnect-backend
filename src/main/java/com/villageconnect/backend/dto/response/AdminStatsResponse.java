package com.villageconnect.backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class AdminStatsResponse {
    private long totalUsers;
    private long totalMerchants;
    private long totalCustomers;
    private long totalShops;
    private long totalProducts;
    private long openShops;
}
