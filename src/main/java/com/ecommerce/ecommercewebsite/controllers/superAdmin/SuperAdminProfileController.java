package com.ecommerce.ecommercewebsite.controllers.superAdmin;

import com.ecommerce.ecommercewebsite.dto.superadmin.SuperAdminProfileResponseDTO;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.superadmin.SuperAdminProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/super-admin")

public class SuperAdminProfileController {
    @Autowired
    private SuperAdminProfileService superAdminProfileService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<SuperAdminProfileResponseDTO>> getSuperAdminProfile(
            Principal principal) {

        String email = principal.getName();

        SuperAdminProfileResponseDTO profile = superAdminProfileService.getProfile(email);

        ApiResponse<SuperAdminProfileResponseDTO> response = new ApiResponse<>("Super Admin Profile Fetched Successfully", profile);

        return ResponseEntity.ok(response);
    }
}
