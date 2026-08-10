package com.ecommerce.ecommercewebsite.controllers.common;

import com.ecommerce.ecommercewebsite.dto.UserFeaturedProductResponseDTO;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.users.FeaturedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FeaturedProductController {
    @Autowired
    private FeaturedProductService featuredProductService;

    // get all featured  products
    @GetMapping("/featured-products")
    public ResponseEntity<ApiResponse<List<UserFeaturedProductResponseDTO>>> getAllFeaturedProducts() {
        List<UserFeaturedProductResponseDTO> response = featuredProductService.getFeaturedProducts();
        ApiResponse<List<UserFeaturedProductResponseDTO>> apiResponse = new ApiResponse<>("Success", response);
        return ResponseEntity.ok(apiResponse);

    }

}
