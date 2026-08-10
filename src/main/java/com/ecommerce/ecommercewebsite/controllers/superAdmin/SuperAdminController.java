package com.ecommerce.ecommercewebsite.controllers.superAdmin;

import com.ecommerce.ecommercewebsite.dto.*;
import com.ecommerce.ecommercewebsite.enums.ApprovalStatus;
import com.ecommerce.ecommercewebsite.enums.ProductStatus;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.CategoryService;
import com.ecommerce.ecommercewebsite.services.FeaturedRequestAdminService;
import com.ecommerce.ecommercewebsite.services.SuperAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/super-admin")
public class SuperAdminController {
    @Autowired
    SuperAdminService superAdminService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FeaturedRequestAdminService featuredRequestAdminService;

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

    @PutMapping("/vendor/{id}/approval")
    ResponseEntity<ApiResponse<String>> updateVendorApproval(@PathVariable Long id, @RequestParam ApprovalStatus status) {
        String response = superAdminService.updateVendorApproval(id, status);
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

    //   get pending   products
    @GetMapping("/pending-products")
    ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> getPendingProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<ProductResponseDTO> products = superAdminService.getPendingProducts(page, size);

        ApiResponse<Page<ProductResponseDTO>> response =
                new ApiResponse<>("Pending products fetched successfully", products);

        return ResponseEntity.ok(response);
    }

    //  approve or reject  the  pending    products
    @PutMapping("/product/{id}/approval")
    ResponseEntity<ApiResponse<String>> updateProductApproval(
            @PathVariable Long id,
            @RequestParam ProductStatus status
    ) {
        String response = superAdminService.updateApprovalProduct(id, status);
        ApiResponse<String> apiResponse = new ApiResponse<>("Product approved successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    //   view all Featured Requests (Pending)
    @GetMapping("/pending-featured-requests")
    ResponseEntity<ApiResponse<Page<FeaturedProductResponseDTO>>> getFeaturedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<FeaturedProductResponseDTO> responseDTOS = featuredRequestAdminService.getPendingFeaturedRequests(page, size);
        ApiResponse<Page<FeaturedProductResponseDTO>> apiResponse = new ApiResponse<>("Pending featured requests fetched successfully", responseDTOS);
        return ResponseEntity.ok(apiResponse);
    }

    //   approve  feature  request
    @PutMapping("/featured-request/{id}/approve")
    ResponseEntity<ApiResponse<String>> approveFeaturedRequest(@PathVariable Long id, @RequestBody FeaturedRequestActionDTO featuredRequest) {
        String response = featuredRequestAdminService.approvedFeaturedRequest(id, featuredRequest);
        ApiResponse<String> apiResponse = new ApiResponse<>("Success", response);
        return ResponseEntity.ok(apiResponse);
    }
    
    // reject  feature   product   request
    @PutMapping("/featured-request/{id}/reject")
    public ResponseEntity<ApiResponse<String>> rejectFeaturedRequest(@PathVariable Long id, @RequestBody FeaturedRequestActionDTO request
    ) {
        String response = featuredRequestAdminService.rejectFeaturedRequest(id, request);
        ApiResponse<String> apiResponse = new ApiResponse<>("Featured request rejected successfully", response);
        return ResponseEntity.ok(apiResponse);
    }


    // add category
    @PostMapping(value = "/add-category", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ApiResponse<CategoryResponseDTO>> addCategory(@ModelAttribute CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO response = superAdminService.addCategory(categoryRequestDTO);
        ApiResponse<CategoryResponseDTO> apiResponse = new ApiResponse<>("Success", response);
        return ResponseEntity.ok(apiResponse);
    }

    //  update category
    @PutMapping(value = "/update-category/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ApiResponse<CategoryResponseDTO>> updateCategory(@ModelAttribute CategoryUpdateRequestDTO categoryUpdateRequestDTO, @PathVariable Long categoryId) {
        CategoryResponseDTO responseDTO = superAdminService.updateCategory(categoryUpdateRequestDTO, categoryId);
        ApiResponse<CategoryResponseDTO> apiResponse = new ApiResponse<>("Success", responseDTO);
        return ResponseEntity.ok(apiResponse);

    }


}