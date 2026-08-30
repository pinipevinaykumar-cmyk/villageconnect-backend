package com.villageconnect.backend.controller;

import com.villageconnect.backend.dto.request.ShopRequest;
import com.villageconnect.backend.dto.response.ApiResponse;
import com.villageconnect.backend.entity.Shop;
import com.villageconnect.backend.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/api/public/shops")
    public ResponseEntity<ApiResponse<List<Shop>>> getShops(
            @RequestParam String village,
            @RequestParam(required = false) Long categoryId) {
        List<Shop> shops;
        if (categoryId != null) {
            shops = shopService.getShopsByVillageAndCategory(village, categoryId);
        } else {
            shops = shopService.getShopsByVillage(village);
        }
        return ResponseEntity.ok(ApiResponse.success("Shops fetched", shops));
    }

    @GetMapping("/api/public/shops/search")
    public ResponseEntity<ApiResponse<List<Shop>>> searchShops(
            @RequestParam String village,
            @RequestParam String keyword) {
        List<Shop> shops = shopService.searchShops(village, keyword);
        return ResponseEntity.ok(ApiResponse.success("Search results", shops));
    }

    @GetMapping("/api/public/shops/{id}")
    public ResponseEntity<ApiResponse<Shop>> getShopById(@PathVariable Long id) {
        Shop shop = shopService.getShopById(id);
        return ResponseEntity.ok(ApiResponse.success("Shop fetched", shop));
    }

    @GetMapping("/api/merchant/shops")
    public ResponseEntity<ApiResponse<List<Shop>>> getMerchantShops(Authentication auth) {
        List<Shop> shops = shopService.getMerchantShops(auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Your shops", shops));
    }

    @PostMapping("/api/merchant/shops")
    public ResponseEntity<ApiResponse<Shop>> createShop(
            @RequestBody ShopRequest request, Authentication auth) {
        Shop shop = shopService.createShop(request, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Shop created successfully", shop));
    }

    @PutMapping("/api/merchant/shops/{id}")
    public ResponseEntity<ApiResponse<Shop>> updateShop(
            @PathVariable Long id,
            @RequestBody ShopRequest request,
            Authentication auth) {
        Shop shop = shopService.updateShop(id, request, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Shop updated", shop));
    }

    @PutMapping("/api/merchant/shops/{id}/toggle-status")
    public ResponseEntity<ApiResponse<Shop>> toggleStatus(
            @PathVariable Long id, Authentication auth) {
        Shop shop = shopService.toggleShopStatus(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Status updated", shop));
    }

    @PutMapping("/api/merchant/shops/{id}/auto-status")
    public ResponseEntity<ApiResponse<Shop>> setAutoStatus(
            @PathVariable Long id, Authentication auth) {
        Shop shop = shopService.setAutoStatus(id, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Auto status enabled", shop));
    }
}