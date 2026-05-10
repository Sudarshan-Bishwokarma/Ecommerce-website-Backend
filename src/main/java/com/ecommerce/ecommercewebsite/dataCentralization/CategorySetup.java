package com.ecommerce.ecommercewebsite.dataCentralization;

import com.ecommerce.ecommercewebsite.model.Category;
import com.ecommerce.ecommercewebsite.repositories.CategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategorySetup {
    @Autowired
    CategoryRepository categoryRepository;

    @PostConstruct
    public void init() {
        if (categoryRepository.count() == 0) {
            String[] defaultCategories = {"Clothing",
                    "Handicrafts",
                    "Traditional Foods",
                    "Paintings",
                    "Jewelry",
                    "Souvenirs",
                    "Wood Crafts",
                    "Pashmina",
                    "Decor Items"};
            for (String name : defaultCategories) {
                Category category = new Category();
                category.setCategoryName(name);
                categoryRepository.save(category);
            }
            System.out.println("Default categories created successfully");
        }
    }
}
