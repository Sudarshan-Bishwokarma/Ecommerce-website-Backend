package com.ecommerce.ecommercewebsite.controllers.vendor;

import com.ecommerce.ecommercewebsite.dto.*;
import com.ecommerce.ecommercewebsite.enums.ProductStatus;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor")
public class VendorProductController {
    @Autowired
    VendorProductService productService;
    @Autowired
    private AdminService adminService;

    @PostMapping(value = "/add-product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponseDTO>> addProduct(

            @RequestPart("product") ProductRequestDTO request,

            @RequestPart("productImage") MultipartFile productImage,

            @RequestPart(value = "variantImages", required = false)
            List<MultipartFile> variantImages,

            @AuthenticationPrincipal UserDetails user
    ) {

        String email = user.getUsername();

        ProductResponseDTO response =
                productService.addProduct(email, request, productImage, variantImages);

        return ResponseEntity.ok(
                new ApiResponse<>("Product Added Successfully", response)
        );
    }

    // publish product
    @PatchMapping("/{id}/update-status")
    public ResponseEntity<ApiResponse<String>> updateStatus(@PathVariable Long id, @RequestBody ProductStatusUpdateRequest request) {
        String response = productService.updateStatus(id, request);
        ApiResponse<String> apiResponse = new ApiResponse<>("Status Updated Successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    //  update product
    @PutMapping(value = "/update-product/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponseDTO>> updateProduct(
            @RequestPart("product") ProductUpdateRequestDTO request,
            @PathVariable Long id, @RequestPart(value = "productImage", required = false) MultipartFile productImage,
            @RequestParam(required = false) Map<String, MultipartFile> variantImages,
            @AuthenticationPrincipal UserDetails user
    ) {
        String email = user.getUsername();
        ProductResponseDTO responseDTO = productService.updateProduct(id, email, request, productImage, variantImages);
        ApiResponse<ProductResponseDTO> apiResponse = new ApiResponse<>("Product Updated Successfully", responseDTO);
        return ResponseEntity.ok().body(apiResponse);
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

