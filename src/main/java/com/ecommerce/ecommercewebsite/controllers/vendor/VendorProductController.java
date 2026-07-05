package com.ecommerce.ecommercewebsite.controllers.vendor;

import com.ecommerce.ecommercewebsite.dto.ProductRequestUpdateDTO;
import com.ecommerce.ecommercewebsite.enums.ProductStatus;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.dto.ProductRequestDTO;
import com.ecommerce.ecommercewebsite.dto.ProductResponseDTO;
import com.ecommerce.ecommercewebsite.services.VendorProductService;
import com.ecommerce.ecommercewebsite.services.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/vendor")
public class VendorProductController {
    @Autowired
    VendorProductService productService;
    @Autowired
    private AdminService adminService;

    // add  product
    @PostMapping(value = "/add-product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponseDTO>> addProduct(@RequestPart("product") ProductRequestDTO request, @RequestPart("image") MultipartFile file, @AuthenticationPrincipal UserDetails user) {
        System.out.println("Controller reached");
        String email = user.getUsername();
        request.setProductImage(file);
        ProductResponseDTO response = productService.addProduct(email, request);
        ApiResponse<ProductResponseDTO> apiResponse = new ApiResponse<>("Product Added Successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // publish product
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<String>> updateStatus(@PathVariable Long id, @RequestParam ProductStatus status) {
        String response = productService.updateStatus(id, status);
        ApiResponse<String> apiResponse = new ApiResponse<>("Product Published Successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/update-product/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody ProductRequestUpdateDTO update, @PathVariable Long id) {
        ProductResponseDTO updateData = productService.updateProduct(id, update);
        return ResponseEntity.ok().body(updateData);
    }

    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        String status = productService.deleteProduct(id);
        return ResponseEntity.ok().body(status);
    }

    // get my  products
    @GetMapping("/my-products")
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> getAllProducts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String email = userDetails.getUsername();
        Page<ProductResponseDTO> responseDTOS = productService.getMyProducts(email, page, size);
        ApiResponse<Page<ProductResponseDTO>> apiResponse = new ApiResponse<>("Products fetched Successfully", responseDTOS);
        return ResponseEntity.ok(apiResponse);
    }


    // get  products
    @GetMapping("/products")
    ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> getProducts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long districtId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String sortType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size

    ) {
        String email = userDetails.getUsername();
        Page<ProductResponseDTO> responseDTOS = productService.getProducts(email, districtId, categoryId, sortType, page, size);
        ApiResponse<Page<ProductResponseDTO>> apiResponse = new ApiResponse<>("Products fetched Successfully", responseDTOS);
        return ResponseEntity.ok(apiResponse);
    }


}

