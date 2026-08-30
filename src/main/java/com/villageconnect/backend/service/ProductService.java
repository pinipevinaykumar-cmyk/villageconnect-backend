package com.villageconnect.backend.service;

import com.villageconnect.backend.dto.request.ProductRequest;
import com.villageconnect.backend.entity.*;
import com.villageconnect.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    public Product addProduct(Long shopId, ProductRequest request, String merchantEmail) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        if (!shop.getMerchant().getEmail().equals(merchantEmail)) {
            throw new RuntimeException("Unauthorized");
        }

        Product product = Product.builder()
                .shop(shop)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .unit(request.getUnit())
                .isAvailable(request.getIsAvailable() != null ? request.getIsAvailable() : true)
                .isActive(true)
                .build();

        return productRepository.save(product);
    }

    public List<Product> getProductsByShop(Long shopId) {
        return productRepository.findByShopIdAndIsActiveTrue(shopId);
    }

    public Product updateProduct(Long productId, ProductRequest request, String merchantEmail) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getShop().getMerchant().getEmail().equals(merchantEmail)) {
            throw new RuntimeException("Unauthorized");
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setUnit(request.getUnit());
        product.setIsAvailable(request.getIsAvailable());

        return productRepository.save(product);
    }

    public void deleteProduct(Long productId, String merchantEmail) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (!product.getShop().getMerchant().getEmail().equals(merchantEmail)) {
            throw new RuntimeException("Unauthorized");
        }

        product.setIsActive(false);
        productRepository.save(product);
    }
}