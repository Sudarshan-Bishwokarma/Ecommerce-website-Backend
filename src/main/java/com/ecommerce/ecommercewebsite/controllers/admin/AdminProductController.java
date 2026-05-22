package com.ecommerce.ecommercewebsite.controllers.admin;

import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.dto.ProductRequestDTO;
import com.ecommerce.ecommercewebsite.dto.ProductResponseDTO;
import com.ecommerce.ecommercewebsite.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin")
public class AdminProductController {
    @Autowired
    ProductService productService;

    @PostMapping(value = "/add-product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponseDTO>> addProduct(@RequestPart("data") String details, @RequestPart("image") MultipartFile file) throws IOException {
        ProductRequestDTO dto = new ObjectMapper().readValue(details, ProductRequestDTO.class);
        ProductResponseDTO response = productService.addProduct(dto, file);
        ApiResponse<ProductResponseDTO> apiResponse = new ApiResponse<>("Product Added Successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/update-product/{id}")
    public ResponseEntity<?> updateProduct(@RequestBody ProductRequestDTO update, @PathVariable Long id) {
        ProductResponseDTO updateData = productService.updateProduct(id, update);
        return ResponseEntity.ok().body(updateData);
    }
    
    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        String status = productService.deleteProduct(id);
        return ResponseEntity.ok().body(status);
    }

}

