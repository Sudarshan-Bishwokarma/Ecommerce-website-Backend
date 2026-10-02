package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.users.ProductReviewRequestDTO;
import com.ecommerce.ecommercewebsite.dto.users.ProductReviewResponse;
import com.ecommerce.ecommercewebsite.model.ProductReview;
import org.springframework.data.domain.Page;

public interface ProductReviewService {
    public ProductReviewResponse addReview(Long productId, Long userId, ProductReviewRequestDTO review);

    public Page<ProductReviewResponse> getReviewsByProduct(Long productId, int page, int size);

    public ProductReviewResponse updateReview(Long reviewId, Long userId, ProductReviewRequestDTO review);

    public void deleteReview(Long reviewId, Long userId);

}
