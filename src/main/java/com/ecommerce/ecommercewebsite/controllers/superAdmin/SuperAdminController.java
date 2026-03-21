package com.ecommerce.ecommercewebsite.controllers.superAdmin;

import com.ecommerce.ecommercewebsite.dto.AdminRequestDTO;
import com.ecommerce.ecommercewebsite.dto.AdminResponseDTO;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/super-admin")
public class SuperAdminController {
    @Autowired
    SuperAdminService superAdminService;

    @PostMapping("/add-admin")
    ResponseEntity<ApiResponse<AdminResponseDTO>> addAdmin(@RequestBody AdminRequestDTO admin) {
        AdminResponseDTO response = superAdminService.addAdmin(admin);
        ApiResponse<AdminResponseDTO> apiResponse = new ApiResponse<>("Admin has been added successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/update-admin/{id}")
    ResponseEntity<ApiResponse<AdminResponseDTO>> updateAdmin(@RequestBody AdminRequestDTO request, @PathVariable Long id) {
        AdminResponseDTO response = superAdminService.updateAdmin(request, id);
        ApiResponse<AdminResponseDTO> apiResponse = new ApiResponse<>("Admin has been updated successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/delete-admin/{id}")
    ResponseEntity<String> deleteAdmin(@PathVariable Long id) {
        String result = superAdminService.deleteAdmin(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all-admins")
    ResponseEntity<ApiResponse<List<AdminResponseDTO>>> getAllAdmins() {
        List<AdminResponseDTO> users = superAdminService.getAllAdmins();
        ApiResponse<List<AdminResponseDTO>> response = new ApiResponse<>("Here  are the all admins!!", users);
        return ResponseEntity.ok(response);
    }
}