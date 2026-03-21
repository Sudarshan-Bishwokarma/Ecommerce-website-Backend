package com.ecommerce.ecommercewebsite.controllers.admin;

import com.ecommerce.ecommercewebsite.dto.AdminResponseDTO;
import com.ecommerce.ecommercewebsite.dto.AdminUpdateDTO;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/edit-profile/{id}")
    public ResponseEntity<ApiResponse<AdminResponseDTO>> editAdminProfile(@RequestPart("admin") AdminUpdateDTO updateDTO, @RequestPart("profile") MultipartFile profile, @PathVariable Long id) {
        AdminResponseDTO responseDTO = adminService.editAdmin(updateDTO, profile, id);
        ApiResponse<AdminResponseDTO> response = new ApiResponse<>("Profile  has been successfully edited", responseDTO);
        return ResponseEntity.ok(response);
    }


}
