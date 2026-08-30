package com.villageconnect.backend.repository;

import com.villageconnect.backend.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    List<Shop> findByVillageAndIsActiveTrue(String village);

    List<Shop> findByVillageAndCategoryIdAndIsActiveTrue(String village, Long categoryId);

    List<Shop> findByMerchantId(Long merchantId);

    @Query("SELECT s FROM Shop s WHERE s.village = :village AND s.isActive = true " +
            "AND (LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(s.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Shop> searchShops(@Param("village") String village,
                           @Param("keyword") String keyword);

    @Query("SELECT s FROM Shop s WHERE s.village = :village " +
            "AND s.currentStatus = 'OPEN' AND s.isActive = true")
    List<Shop> findOpenShops(@Param("village") String village);
}