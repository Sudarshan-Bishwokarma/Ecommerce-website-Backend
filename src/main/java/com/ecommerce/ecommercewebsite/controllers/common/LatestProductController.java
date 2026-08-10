package com.ecommerce.ecommercewebsite.controllers.common;

import com.ecommerce.ecommercewebsite.dto.UserFeaturedProductResponseDTO;
import com.ecommerce.ecommercewebsite.dto.UserLatestProductResponseDTO;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.users.LatestProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@RestController
public class LatestProductController {
    @Autowired
    private LatestProductService latestProductService;

    //  get  latest     products
    @GetMapping("/latest-products")
    ResponseEntity<ApiResponse<List<UserLatestProductResponseDTO>>> getLatestProducts() {
        List<UserLatestProductResponseDTO> responseDTO = latestProductService.getLatestProducts();
        ApiResponse<List<UserLatestProductResponseDTO>> apiResponse = new ApiResponse<>("success", responseDTO);
        return ResponseEntity.ok(apiResponse);


    }

}
