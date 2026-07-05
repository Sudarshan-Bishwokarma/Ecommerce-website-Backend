package com.ecommerce.ecommercewebsite.controllers.vendor;

import com.ecommerce.ecommercewebsite.dto.VendorResponseDTO;
import com.ecommerce.ecommercewebsite.dto.AdminUpdateDTO;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/vendor")
public class VendorController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/edit-profile/{id}")
    public ResponseEntity<ApiResponse<VendorResponseDTO>> editAdminProfile(@RequestPart("admin") AdminUpdateDTO updateDTO, @RequestPart("profile") MultipartFile profile, @PathVariable Long id) {
        VendorResponseDTO responseDTO = adminService.editAdmin(updateDTO, profile, id);
        ApiResponse<VendorResponseDTO> response = new ApiResponse<>("Profile  has been successfully edited", responseDTO);
        return ResponseEntity.ok(response);
    }

    // business profile
}
