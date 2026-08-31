package com.villageconnect.backend.controller;

import com.villageconnect.backend.dto.response.AdminStatsResponse;
import com.villageconnect.backend.dto.response.AdminUserResponse;
import com.villageconnect.backend.dto.response.ApiResponse;
import com.villageconnect.backend.entity.Shop;
import com.villageconnect.backend.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<AdminStatsResponse>> getStats() {
        return ResponseEntity.ok(ApiResponse.success("Stats fetched", adminService.getStats()));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<AdminUserResponse>>> getUsers() {
        return ResponseEntity.ok(ApiResponse.success("Users fetched", adminService.getAllUsers()));
    }

    @GetMapping("/shops")
    public ResponseEntity<ApiResponse<List<Shop>>> getShops() {
        return ResponseEntity.ok(ApiResponse.success("Shops fetched", adminService.getAllShops()));
    }

    @PutMapping("/users/{id}/toggle-active")
    public ResponseEntity<ApiResponse<Void>> toggleUserActive(@PathVariable Long id) {
        adminService.toggleUserActive(id);
        return ResponseEntity.ok(ApiResponse.success("User status updated", null));
    }
}
