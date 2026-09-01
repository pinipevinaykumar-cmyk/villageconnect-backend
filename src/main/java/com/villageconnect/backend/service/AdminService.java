package com.villageconnect.backend.service;

import com.villageconnect.backend.dto.response.AdminStatsResponse;
import com.villageconnect.backend.dto.response.AdminUserResponse;
import com.villageconnect.backend.entity.Shop;
import com.villageconnect.backend.entity.User;
import com.villageconnect.backend.repository.ProductRepository;
import com.villageconnect.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ShopService shopService;

    public AdminStatsResponse getStats() {
        List<User> users = userRepository.findAll();
        List<Shop> shops = shopService.getAllShops();

        return AdminStatsResponse.builder()
                .totalUsers(users.size())
                .totalMerchants(users.stream().filter(u -> u.getRole() == User.Role.MERCHANT).count())
                .totalCustomers(users.stream().filter(u -> u.getRole() == User.Role.CUSTOMER).count())
                .totalShops(shops.size())
                .totalProducts(productRepository.count())
                .openShops(shops.stream().filter(s -> s.getCurrentStatus() == Shop.ShopStatus.OPEN).count())
                .build();
    }

    public List<AdminUserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> AdminUserResponse.builder()
                        .id(u.getId())
                        .name(u.getName())
                        .email(u.getEmail())
                        .phone(u.getPhone())
                        .role(u.getRole().name())
                        .village(u.getVillage())
                        .isActive(u.getIsActive())
                        .createdAt(u.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public List<Shop> getAllShops() {
        return shopService.getAllShops();
    }

    public void toggleUserActive(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setIsActive(!user.getIsActive());
        userRepository.save(user);
    }
}
