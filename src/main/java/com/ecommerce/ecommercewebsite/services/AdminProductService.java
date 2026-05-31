package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.ProductRequestDTO;
import com.ecommerce.ecommercewebsite.dto.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminProductService {
    // admin only methods
    public ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO, MultipartFile file);

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO);

    public String deleteProduct(Long id);

    public Page<ProductResponseDTO> getMyProducts(String email, int page, int size);

    public Page<ProductResponseDTO> getMySortProducts(String email, String sortType, int page, int size);

    public Page<ProductResponseDTO> getProducts(String email, Long districtId, Long categoryId, String sortType, int page, int size);


}
