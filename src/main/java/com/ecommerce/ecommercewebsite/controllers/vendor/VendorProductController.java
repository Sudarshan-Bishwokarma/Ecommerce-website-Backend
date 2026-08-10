package com.ecommerce.ecommercewebsite.controllers.vendor;

import com.ecommerce.ecommercewebsite.dto.*;
import com.ecommerce.ecommercewebsite.enums.ProductStatus;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.FeaturedRequestVendorService;
import com.ecommerce.ecommercewebsite.services.VendorProductService;
import com.ecommerce.ecommercewebsite.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vendor")
public class VendorProductController {
    @Autowired
    VendorProductService productService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private FeaturedRequestVendorService featuredProductService;


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

    //  update product
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<String>> updateStatus(@PathVariable Long id, Principal principal, @RequestParam ProductStatus status) {
        String email = principal.getName();
        String response = productService.updateStatus(id, email, status);
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

    //  delete   product
    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
        String email = user.getUsername();
        String status = productService.deleteProduct(id, email);
        ApiResponse<String> apiResponse = new ApiResponse<>("Product Deleted Successfully", null);
        return ResponseEntity.ok().body(apiResponse);
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

    // get product details
    @GetMapping("/product-detail/{id}")
    ResponseEntity<ApiResponse<VendorProductDetailResponseDTO>> getProductDetailsById(@PathVariable Long id) {
        VendorProductDetailResponseDTO details = productService.getProductDetailsById(id);
        ApiResponse<VendorProductDetailResponseDTO> apiResponse = new ApiResponse<>("Product Fetched Successfully", details);
        return ResponseEntity.ok(apiResponse);
    }

    // get  products
    @GetMapping("/products")
    ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> getProducts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) Long districtId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String sortType,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size

    ) {
        String email = userDetails.getUsername();
        Page<ProductResponseDTO> responseDTOS = productService.getProducts(email, districtId, categoryId, sortType, search, page, size);
        ApiResponse<Page<ProductResponseDTO>> apiResponse = new ApiResponse<>("Products fetched Successfully", responseDTOS);
        return ResponseEntity.ok(apiResponse);
    }
    // request for product  approval

    @PostMapping("/product/{id}/request-approval")
    public ResponseEntity<ApiResponse<String>> requestApproval(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        String response = productService.requestApproval(id, email);
        ApiResponse<String> apiResponse = new ApiResponse<>("Product approval status updated successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    //
    @PatchMapping("/{id}/resubmit")
    public ResponseEntity<ApiResponse<String>> resubmitProduct(@PathVariable Long id, Principal principal) {
        String email = principal.getName();

        String response = productService.reSubmitProduct(id, email);

        return ResponseEntity.ok(new ApiResponse<>("Product resubmitted", response)
        );
    }

}

