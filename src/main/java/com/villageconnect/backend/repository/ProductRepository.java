package com.villageconnect.backend.repository;

import com.villageconnect.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByShopIdAndIsActiveTrue(Long shopId);
    List<Product> findByShopIdAndIsAvailableTrue(Long shopId);
}