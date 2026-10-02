package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.users.ProductReviewRequestDTO;
import com.ecommerce.ecommercewebsite.dto.users.ProductReviewResponse;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.model.ProductReview;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.ProductRepository;
import com.ecommerce.ecommercewebsite.repositories.ProductReviewRepository;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductReviewServiceImpl implements ProductReviewService {
    @Autowired
    private ProductReviewRepository productReviewRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ProductReviewResponse addReview(Long productId, Long userId, ProductReviewRequestDTO review) {
        Product product = productRepository.findByProductId(productId).orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        if (productReviewRepository.existsProductReviewByProductProductIdAndUserId(productId, user.getId())) {
            throw new ApiException(ProductErrorCode.PRODUCT_ALREADY_REVIEWED);
        }
        ProductReview addReview = new ProductReview();
        addReview.setRating(review.getRating());
        addReview.setComment(review.getComment());
        addReview.setUser(user);
        addReview.setCreatedAt(LocalDateTime.now());
        addReview.setProduct(product);
        ProductReview savedReview = productReviewRepository.save(addReview);

        return new ProductReviewResponse(
                savedReview.getId(),
                savedReview.getRating(),
                savedReview.getComment(),
                savedReview.getUser().getName(),
                savedReview.getCreatedAt()
        );
    }

    @Override
    public Page<ProductReviewResponse> getReviewsByProduct(Long productId, int page, int size) {
        Product product = productRepository.findByProductId(productId).orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductReview> reviews = productReviewRepository.findByProductProductId(productId, pageable);
        return reviews.map(review -> new ProductReviewResponse(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getUser().getName(),
                review.getCreatedAt()
        ));

    }

    @Override
    public ProductReviewResponse updateReview(Long reviewId, Long userId, ProductReviewRequestDTO review) {
        ProductReview productReview = productReviewRepository.findById(reviewId).orElseThrow(() -> new ApiException(ProductErrorCode.REVIEW_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        // ownership check
        if (!productReview.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not allowed to update this review");
        }
        productReview.setComment(review.getComment());
        productReview.setRating(review.getRating());
        ProductReview savedReview = productReviewRepository.save(productReview);

        return new ProductReviewResponse(
                savedReview.getId(),
                savedReview.getRating(),
                savedReview.getComment(),
                savedReview.getUser().getName(),
                savedReview.getCreatedAt()
        );
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) {
        ProductReview review = productReviewRepository.findById(reviewId).orElseThrow(() -> new ApiException(ProductErrorCode.REVIEW_NOT_FOUND));
        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        if (!review.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not allowed to delete this review");
        }
        productReviewRepository.deleteById(reviewId);


    }

}
