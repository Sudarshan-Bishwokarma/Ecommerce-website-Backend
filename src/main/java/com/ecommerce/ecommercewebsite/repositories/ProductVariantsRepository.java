package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.model.Cart;
import com.ecommerce.ecommercewebsite.model.ProductVariants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductVariantsRepository extends JpaRepository<ProductVariants, Long> {
    public List<ProductVariants> findByPriceBetween(Double minPrice, Double maxPrice);


}
