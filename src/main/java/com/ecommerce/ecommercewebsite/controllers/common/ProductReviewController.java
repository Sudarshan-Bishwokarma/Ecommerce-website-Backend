package com.ecommerce.ecommercewebsite.controllers.common;

import com.ecommerce.ecommercewebsite.dto.users.ProductReviewRequestDTO;
import com.ecommerce.ecommercewebsite.dto.users.ProductReviewResponse;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.model.ProductReview;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.ProductReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/reviews")
public class ProductReviewController {
    @Autowired
    private ProductReviewService productReviewService;
    @Autowired
    private UserRepository userRepository;

    // add review
    @PostMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<ProductReviewResponse>> createProductReview(@PathVariable Long productId, ProductReviewRequestDTO productReviewRequestDTO, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        Long userId = user.getId();
        ProductReviewResponse response = productReviewService.addReview(productId, userId, productReviewRequestDTO);
        ApiResponse<ProductReviewResponse> apiResponse = new ApiResponse<>("Product has been reviewed successfully", response);
        return ResponseEntity.ok(apiResponse);

    }

    // get  review by product
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<Page<ProductReviewResponse>>> getReview(@PathVariable Long productId,
                                                                              @RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "10") int size) {
        Page<ProductReviewResponse> response = productReviewService.getReviewsByProduct(productId, page, size);
        ApiResponse<Page<ProductReviewResponse>> apiResponse = new ApiResponse<>("Success", response);
        return ResponseEntity.ok(apiResponse);

    }

    // update review
    @PutMapping("/update/{reviewId}")
    public ResponseEntity<ApiResponse<ProductReviewResponse>> updateReview(@PathVariable Long reviewId, ProductReviewRequestDTO productReviewRequestDTO, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        Long userId = user.getId();
        ProductReviewResponse response = productReviewService.updateReview(reviewId, userId, productReviewRequestDTO);
        ApiResponse<ProductReviewResponse> apiResponse = new ApiResponse<>("Product Review has been updated successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // delete review
    @DeleteMapping("/delete/{reviewId}")
    ResponseEntity<ApiResponse<String>> deleteReview(@PathVariable Long reviewId, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        Long userId = user.getId();
        productReviewService.deleteReview(reviewId, userId);
        ApiResponse<String> apiResponse = new ApiResponse<>("Product Review has been deleted successfully", null);
        return ResponseEntity.ok(apiResponse);
    }

}
