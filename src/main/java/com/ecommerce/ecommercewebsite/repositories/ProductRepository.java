package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory_id(Long categoryId);

    public Optional<Product> findByProductName(String productName);

    List<Product> findByProductPriceBetween(Double minPrice, Double maxPrice);
}

