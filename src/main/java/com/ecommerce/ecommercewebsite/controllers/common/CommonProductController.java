package com.ecommerce.ecommercewebsite.controllers.common;

import com.ecommerce.ecommercewebsite.dto.ProductResponseDTO;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommonProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/sort-products/{sortType}")
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> sortProducts(
            @PathVariable String sortType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ProductResponseDTO> products = productService.sortProducts(sortType, page, size);
        ApiResponse<Page<ProductResponseDTO>> apiResponse = new ApiResponse<>("Success", products);
        return ResponseEntity.ok(apiResponse);
    }

}
