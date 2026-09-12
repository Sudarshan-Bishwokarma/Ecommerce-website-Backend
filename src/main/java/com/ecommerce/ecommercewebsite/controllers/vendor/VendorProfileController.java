package com.ecommerce.ecommercewebsite.controllers.vendor;

import com.ecommerce.ecommercewebsite.dto.vendor.VendorProfileResponseDTO;
import com.ecommerce.ecommercewebsite.dto.vendor.VendorProfileUpdateRequestDTO;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.vendor.VendorProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/vendor")
public class VendorProfileController {
    @Autowired
    private VendorProfileService vendorProfileService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<VendorProfileResponseDTO>> getVendorProfile(
            Principal principal) {

        String email = principal.getName();

        User vendor = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.VENDOR_NOT_FOUND));

        VendorProfileResponseDTO profile = vendorProfileService.getVendorProfile(vendor);

        ApiResponse<VendorProfileResponseDTO> response = new ApiResponse<>("success", profile);

        return ResponseEntity.ok(response);
    }

    @PutMapping(
            value = "/profile",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<ApiResponse<VendorProfileResponseDTO>> updateVendorProfile(
            Principal principal,
            @ModelAttribute VendorProfileUpdateRequestDTO request) {

        String email = principal.getName();

        User vendor = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.VENDOR_NOT_FOUND));

        VendorProfileResponseDTO updatedProfile = vendorProfileService.updateVendorProfile(vendor, request);


        ApiResponse<VendorProfileResponseDTO> response = new ApiResponse<>("Vendor profile updated successfully", updatedProfile);

        return ResponseEntity.ok(response);
    }
}
