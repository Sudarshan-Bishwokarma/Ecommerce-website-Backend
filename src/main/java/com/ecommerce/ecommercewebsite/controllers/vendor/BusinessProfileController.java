package com.ecommerce.ecommercewebsite.controllers.vendor;

import com.ecommerce.ecommercewebsite.dto.BusinessProfileRequestDTO;
import com.ecommerce.ecommercewebsite.dto.VendorResponseDTO;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.BusinessProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor")
public class BusinessProfileController {
    @Autowired
    private BusinessProfileService businessProfileService;

    // onboarding business profile
    @PostMapping("/complete/business-profile")
    public ResponseEntity<ApiResponse<VendorResponseDTO>> vendorOnboarding(@AuthenticationPrincipal UserDetails details, @ModelAttribute BusinessProfileRequestDTO businessProfileRequestDTO) {
        
        String email = details.getUsername();
        VendorResponseDTO responseDTO = businessProfileService.vendorOnboarding(email, businessProfileRequestDTO);
        ApiResponse<VendorResponseDTO> apiResponse = new ApiResponse<>(" Profile is Completed Successfully", responseDTO);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    // business   profile status
    @GetMapping("/business-status")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> getBusinessStatus(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        boolean completed = businessProfileService.isBusinessProfileCompleted(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("businessProfileStatus", completed);
        ApiResponse<Map<String, Boolean>> apiResponse = new ApiResponse<>(" Business Profile Completed Successfully", response);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
