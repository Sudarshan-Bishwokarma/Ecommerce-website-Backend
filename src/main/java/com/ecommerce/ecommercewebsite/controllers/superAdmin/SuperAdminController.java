package com.ecommerce.ecommercewebsite.controllers.superAdmin;

import com.ecommerce.ecommercewebsite.dto.VendorResponseDTO;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/super-admin")
public class SuperAdminController {
    @Autowired
    SuperAdminService superAdminService;

    @DeleteMapping("/delete-vendor/{id}")
    ResponseEntity<String> deleteVendor(@PathVariable Long id) {
        String result = superAdminService.deleteVendor(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all-vendors")
    ResponseEntity<ApiResponse<Page<VendorResponseDTO>>> getAllVendors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<VendorResponseDTO> users = superAdminService.getAllVendors(page, size);
        ApiResponse<Page<VendorResponseDTO>> response = new ApiResponse<>("Here  are the all  vendors!!", users);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/approve-vendor/{id}")
    ResponseEntity<ApiResponse<String>> approveVendor(@PathVariable Long id) {
        String response = superAdminService.approveVendor(id);
        ApiResponse<String> apiResponse = new ApiResponse<>("Success", response);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/pending-vendors")
    ResponseEntity<ApiResponse<Page<VendorResponseDTO>>> getAllPendingVendors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<VendorResponseDTO> response = superAdminService.getAllPendingVendors(page, size);
        ApiResponse<Page<VendorResponseDTO>> apiResponse = new ApiResponse<>("Success", response);
        return ResponseEntity.ok(apiResponse);
    }
}