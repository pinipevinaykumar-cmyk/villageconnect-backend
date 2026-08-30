package com.villageconnect.backend.service;

import com.villageconnect.backend.entity.Category;
import com.villageconnect.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findByIsActiveTrue();
    }

    public Category createCategory(String name, String icon, String description) {
        Category category = Category.builder()
                .name(name)
                .icon(icon)
                .description(description)
                .isActive(true)
                .build();
        return categoryRepository.save(category);
    }
}