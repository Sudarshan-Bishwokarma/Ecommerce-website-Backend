package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.ProductRequestDTO;
import com.ecommerce.ecommercewebsite.dto.ProductRequestUpdateDTO;
import com.ecommerce.ecommercewebsite.dto.ProductResponseDTO;
import com.ecommerce.ecommercewebsite.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface VendorProductService {
    // vendor only methods
    public ProductResponseDTO addProduct(String email, ProductRequestDTO productRequestDTO);

    public String updateStatus(Long id, ProductStatus status);

    public ProductResponseDTO updateProduct(Long id, ProductRequestUpdateDTO productRequestUpdateDTO);

    public String deleteProduct(Long id);

    public Page<ProductResponseDTO> getMyProducts(String email, int page, int size);

    public Page<ProductResponseDTO> getProducts(String email, Long districtId, Long categoryId, String sortType, int page, int size);


}
