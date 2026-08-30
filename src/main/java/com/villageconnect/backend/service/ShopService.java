package com.villageconnect.backend.service;

import com.villageconnect.backend.dto.request.ShopRequest;
import com.villageconnect.backend.entity.*;
import com.villageconnect.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public Shop createShop(ShopRequest request, String merchantEmail) {
        User merchant = userRepository.findByEmail(merchantEmail)
                .orElseThrow(() -> new RuntimeException("Merchant not found"));

        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId()).orElse(null);
        }

        Shop shop = Shop.builder()
                .name(request.getName())
                .ownerName(request.getOwnerName())
                .phone(request.getPhone())
                .whatsapp(request.getWhatsapp())
                .description(request.getDescription())
                .address(request.getAddress())
                .village(request.getVillage())
                .district(request.getDistrict())
                .state(request.getState())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .category(category)
                .merchant(merchant)
                .openTime(request.getOpenTime())
                .closeTime(request.getCloseTime())
                .is24Hours(request.getIs24Hours() != null ? request.getIs24Hours() : false)
                .isDeliveryAvailable(request.getIsDeliveryAvailable() != null
                        ? request.getIsDeliveryAvailable() : false)
                .currentStatus(Shop.ShopStatus.CLOSED)
                .isManuallySet(false)
                .isActive(true)
                .build();

        return shopRepository.save(shop);
    }

    public List<Shop> getShopsByVillage(String village) {
        List<Shop> shops = shopRepository.findByVillageAndIsActiveTrue(village);
        shops.forEach(this::updateAutoStatus);
        return shops;
    }

    public List<Shop> getShopsByVillageAndCategory(String village, Long categoryId) {
        List<Shop> shops =
                shopRepository.findByVillageAndCategoryIdAndIsActiveTrue(village, categoryId);
        shops.forEach(this::updateAutoStatus);
        return shops;
    }

    public List<Shop> searchShops(String village, String keyword) {
        List<Shop> shops = shopRepository.searchShops(village, keyword);
        shops.forEach(this::updateAutoStatus);
        return shops;
    }

    public Shop getShopById(Long id) {
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shop not found"));
        updateAutoStatus(shop);
        return shop;
    }

    public List<Shop> getMerchantShops(String merchantEmail) {
        User merchant = userRepository.findByEmail(merchantEmail)
                .orElseThrow(() -> new RuntimeException("Merchant not found"));
        return shopRepository.findByMerchantId(merchant.getId());
    }

    public Shop toggleShopStatus(Long shopId, String merchantEmail) {
        Shop shop = getShopAndVerifyOwner(shopId, merchantEmail);

        if (shop.getCurrentStatus() == Shop.ShopStatus.OPEN) {
            shop.setCurrentStatus(Shop.ShopStatus.CLOSED);
        } else {
            shop.setCurrentStatus(Shop.ShopStatus.OPEN);
        }
        shop.setIsManuallySet(true);

        return shopRepository.save(shop);
    }

    public Shop setAutoStatus(Long shopId, String merchantEmail) {
        Shop shop = getShopAndVerifyOwner(shopId, merchantEmail);
        shop.setIsManuallySet(false);
        updateAutoStatus(shop);
        return shopRepository.save(shop);
    }

    public Shop updateShop(Long shopId, ShopRequest request, String merchantEmail) {
        Shop shop = getShopAndVerifyOwner(shopId, merchantEmail);

        shop.setName(request.getName());
        shop.setOwnerName(request.getOwnerName());
        shop.setPhone(request.getPhone());
        shop.setWhatsapp(request.getWhatsapp());
        shop.setDescription(request.getDescription());
        shop.setAddress(request.getAddress());
        shop.setOpenTime(request.getOpenTime());
        shop.setCloseTime(request.getCloseTime());
        shop.setIsDeliveryAvailable(request.getIsDeliveryAvailable());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElse(null);
            shop.setCategory(category);
        }

        return shopRepository.save(shop);
    }

    private void updateAutoStatus(Shop shop) {
        if (shop.getIsManuallySet()) return;

        if (shop.getIs24Hours()) {
            shop.setCurrentStatus(Shop.ShopStatus.OPEN);
            return;
        }

        if (shop.getOpenTime() == null || shop.getCloseTime() == null) return;

        LocalTime now = LocalTime.now();
        LocalTime openTime = LocalTime.parse(shop.getOpenTime(),
                DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime closeTime = LocalTime.parse(shop.getCloseTime(),
                DateTimeFormatter.ofPattern("HH:mm"));

        if (now.isAfter(openTime) && now.isBefore(closeTime)) {
            shop.setCurrentStatus(Shop.ShopStatus.OPEN);
        } else {
            shop.setCurrentStatus(Shop.ShopStatus.CLOSED);
        }
    }

    private Shop getShopAndVerifyOwner(Long shopId, String merchantEmail) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        if (!shop.getMerchant().getEmail().equals(merchantEmail)) {
            throw new RuntimeException("Unauthorized: You don't own this shop");
        }

        return shop;
    }
}