package com.villageconnect.backend.config;

import com.villageconnect.backend.entity.Category;
import com.villageconnect.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            String[][] categories = {
                    {"Grocery", "🛒", "General grocery and kirana stores"},
                    {"Vegetables", "🥦", "Fresh vegetables and fruits"},
                    {"Chicken & Meat", "🍗", "Chicken, mutton, and fish shops"},
                    {"Medical", "💊", "Medical and pharmacy stores"},
                    {"Hotel & Food", "🍛", "Hotels, tiffin centers, restaurants"},
                    {"Milk & Dairy", "🥛", "Milk, curd, butter, and dairy products"},
                    {"Bakery", "🍞", "Bakery and sweets shops"},
                    {"Hardware", "🔧", "Hardware and construction materials"},
                    {"Clothing", "👗", "Clothes and textile shops"},
                    {"Electronics", "📱", "Mobile, TV, and electronics shops"},
                    {"Salon", "✂️", "Hair salons and beauty parlors"},
                    {"Petrol Bunk", "⛽", "Petrol and diesel stations"}
            };

            for (String[] cat : categories) {
                Category category = Category.builder()
                        .name(cat[0])
                        .icon(cat[1])
                        .description(cat[2])
                        .isActive(true)
                        .build();
                categoryRepository.save(category);
            }
            System.out.println("✅ Default categories inserted");
        }
    }
}