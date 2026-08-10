package com.ecommerce.ecommercewebsite.controllers.vendor;

import com.ecommerce.ecommercewebsite.dto.FeaturedRequestDTO;
import com.ecommerce.ecommercewebsite.dto.FeaturedRequestVendorResponseDTO;
import com.ecommerce.ecommercewebsite.dto.VendorFeaturedRequestPaymentResponseDTO;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.FeaturedRequestVendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/vendor")
public class VendorFeaturedRequestController {
    @Autowired
    private FeaturedRequestVendorService featuredRequestVendorService;
    private PasswordEncoder passwordEncoder;
    //     request  for featured

    @PostMapping("/product/{id}/featured-request")
    public ResponseEntity<ApiResponse<String>> requestFeaturedProduct(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, @RequestBody FeaturedRequestDTO request) {
        String email = userDetails.getUsername();
        String response = featuredRequestVendorService.requestFeaturedProduct(id, email, request);
        ApiResponse<String> apiResponse = new ApiResponse<>("Featured Product Request Successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // get my feature requests
    @GetMapping("/my-featured-requests")
    public ResponseEntity<ApiResponse<Page<FeaturedRequestVendorResponseDTO>>> getFeaturedRequests(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String email = principal.getName();
        Page<FeaturedRequestVendorResponseDTO> response = featuredRequestVendorService.getMYFeaturedRequests(email, page, size);
        ApiResponse<Page<FeaturedRequestVendorResponseDTO>> apiResponse = new ApiResponse<>();
        return ResponseEntity.ok(apiResponse);


    }

    // get  featured  request details
    @GetMapping("/featured-request/{id}")
    ResponseEntity<ApiResponse<VendorFeaturedRequestPaymentResponseDTO>> getFeaturedRequest(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        VendorFeaturedRequestPaymentResponseDTO response = featuredRequestVendorService.getFeaturedRequestDetails(id, email);
        ApiResponse<VendorFeaturedRequestPaymentResponseDTO> apiResponse = new ApiResponse<>("Featured Request Details Successfully", response);
        return ResponseEntity.ok(apiResponse);

    }

}
