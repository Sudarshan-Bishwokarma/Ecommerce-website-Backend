package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVariantsRepository extends JpaRepository<ProductVariant, Long> {
    public List<ProductVariant> findByPriceBetween(Double minPrice, Double maxPrice);


}
