package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.dto.users.ProductReviewResponse;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.model.ProductReview;
import com.ecommerce.ecommercewebsite.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {
    List<ProductReview> findByProductProductId(Long productId);

    Page<ProductReview> findByProductProductId(Long productId, Pageable pageable);

    Optional<ProductReview> findByProductProductIdAndUserId(Long productId, Long userId);

    boolean existsProductReviewByProductProductIdAndUserId(Long productId, Long userId);

    Optional<ProductReview> findByIdAndUserId(Long id, User user);
}

