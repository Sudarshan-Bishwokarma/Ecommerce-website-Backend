package com.ecommerce.ecommercewebsite.controllers.superAdmin;

import com.ecommerce.ecommercewebsite.dto.CategoryRequestDTO;
import com.ecommerce.ecommercewebsite.dto.CategoryResponseDTO;
import com.ecommerce.ecommercewebsite.dto.CategoryUpdateRequestDTO;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.superadmin.SuperAdminCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/super-admin/")
public class SuperAdminCategoryController {
    @Autowired
    private SuperAdminCategoryService superAdminCategoryService;

    // add category
    @PostMapping(value = "/add-category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ApiResponse<CategoryResponseDTO>> addCategory(@ModelAttribute CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO response = superAdminCategoryService.addCategory(categoryRequestDTO);
        ApiResponse<CategoryResponseDTO> apiResponse = new ApiResponse<>("Success", response);
        return ResponseEntity.ok(apiResponse);
    }

    //  update category
    @PutMapping(value = "/update-category/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ApiResponse<CategoryResponseDTO>> updateCategory(@ModelAttribute CategoryUpdateRequestDTO categoryUpdateRequestDTO, @PathVariable Long categoryId) {
        CategoryResponseDTO responseDTO = superAdminCategoryService.updateCategory(categoryUpdateRequestDTO, categoryId);
        ApiResponse<CategoryResponseDTO> apiResponse = new ApiResponse<>("Success", responseDTO);
        return ResponseEntity.ok(apiResponse);

    }

    @GetMapping("/category/{categoryId}")
    ResponseEntity<ApiResponse<CategoryResponseDTO>> getCategory(
            @PathVariable Long categoryId) {

        CategoryResponseDTO response = superAdminCategoryService.getCategory(categoryId);

        ApiResponse<CategoryResponseDTO> apiResponse = new ApiResponse<>("Success", response);

        return ResponseEntity.ok(apiResponse);
    }
}
