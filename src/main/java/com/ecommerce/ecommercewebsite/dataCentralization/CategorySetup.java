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
            String[] defaultCategories = {
                    "Traditional Textiles & Clothing",
                    "Handicrafts",
                    "Pashmina & Wool Products",
                    "Wood & Bamboo Crafts",
                    "Metal Crafts",
                    "Pottery & Ceramics",
                    "Thangka & Traditional Art",
                    "Jewelry & Accessories",
                    "Souvenirs & Tourist Products",
                    "Religious & Cultural Items",
                    "Traditional Musical Instruments",
                    "Handmade Bags & Accessories",
                    "Local Food & Beverages",
                    "Agricultural Products",
                    "Herbs, Spices & Natural Products"
            };
            for (String name : defaultCategories) {
                Category category = new Category();
                category.setCategoryName(name);
                categoryRepository.save(category);
            }
            System.out.println("Default categories created successfully");
        }
    }
}
