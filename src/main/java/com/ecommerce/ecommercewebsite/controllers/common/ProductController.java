package com.ecommerce.ecommercewebsite.controllers.common;

import com.ecommerce.ecommercewebsite.dto.VendorProductDetailResponseDTO;
import com.ecommerce.ecommercewebsite.dto.ProductResponseDTO;
import com.ecommerce.ecommercewebsite.dto.users.ProductDetailResponseDTO;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/product/{id}")
    public ResponseEntity<ApiResponse<ProductDetailResponseDTO>> getProductDetails(@PathVariable Long id) {
        ProductDetailResponseDTO responseDTO = productService.getProductDetails(id);
        ApiResponse<ProductDetailResponseDTO> apiResponse = new ApiResponse<>("success", responseDTO);
        return ResponseEntity.ok(apiResponse);
    }

    // fetch  product  by category  Id
    @GetMapping("/all-products/category/{id}")
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getProductsByCategoryId(@PathVariable Long id) {
        List<ProductResponseDTO> allProducts = productService.getProductsByCategoryId(id);
        ApiResponse<List<ProductResponseDTO>> response = new ApiResponse<>("Products Fetched Successfully By Category", allProducts);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/all-products")
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ProductResponseDTO> allProducts = productService.getAllProducts(page, size);
        ApiResponse<Page<ProductResponseDTO>> apiResponse = new ApiResponse<>("Products Fetched Successfully", allProducts);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/filter/price")
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getAllProductsByPrice(@RequestParam Double minPrice, @RequestParam Double maxPrice) {
        List<ProductResponseDTO> products = productService.filterProductsByPrice(minPrice, maxPrice);
        ApiResponse<List<ProductResponseDTO>> productsResponse = new ApiResponse<>("Products Fetched Successfully By Price", products);
        return ResponseEntity.ok(productsResponse);
    }

    // fetch  products  by  districtId
    @GetMapping("/products/district/{id}")
    ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> getAllDistrictProducts(@PathVariable Long id,
                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "10") int size) {
        Page<ProductResponseDTO> response = productService.getAllProductsByDistrict(id, page, size);
        ApiResponse<Page<ProductResponseDTO>> apiResponse = new ApiResponse<>("Success", response);
        return ResponseEntity.ok(apiResponse);
    }

}
