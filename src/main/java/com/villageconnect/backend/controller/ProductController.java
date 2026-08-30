package com.villageconnect.backend.controller;

import com.villageconnect.backend.dto.request.ProductRequest;
import com.villageconnect.backend.dto.response.ApiResponse;
import com.villageconnect.backend.entity.Product;
import com.villageconnect.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/public/shops/{shopId}/products")
    public ResponseEntity<ApiResponse<List<Product>>> getProducts(
            @PathVariable Long shopId) {
        List<Product> products = productService.getProductsByShop(shopId);
        return ResponseEntity.ok(ApiResponse.success("Products fetched", products));
    }

    @PostMapping("/api/merchant/shops/{shopId}/products")
    public ResponseEntity<ApiResponse<Product>> addProduct(
            @PathVariable Long shopId,
            @RequestBody ProductRequest request,
            Authentication auth) {
        Product product = productService.addProduct(shopId, request, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Product added", product));
    }

    @PutMapping("/api/merchant/products/{productId}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductRequest request,
            Authentication auth) {
        Product product = productService.updateProduct(productId, request, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Product updated", product));
    }

    @DeleteMapping("/api/merchant/products/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable Long productId,
            Authentication auth) {
        productService.deleteProduct(productId, auth.getName());
        return ResponseEntity.ok(ApiResponse.success("Product deleted", null));
    }
}