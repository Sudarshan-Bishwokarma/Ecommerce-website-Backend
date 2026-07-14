package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.*;
import com.ecommerce.ecommercewebsite.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface VendorProductService {
    // vendor only methods
    public ProductResponseDTO addProduct(
            String email,
            ProductRequestDTO productRequestDTO,
            MultipartFile productImage,
            List<MultipartFile> variantImages
    );

    public String updateStatus(Long id, ProductStatusUpdateRequest request);

    public ProductResponseDTO updateProduct(Long id, String email, ProductUpdateRequestDTO request, MultipartFile productImage, Map<String, MultipartFile> files);

    public String deleteProduct(Long id);

    public Page<ProductResponseDTO> getMyProducts(String email, int page, int size);

    public Page<ProductResponseDTO> getProducts(String email, Long districtId, Long categoryId, String sortType, int page, int size);

}
