package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.ProductDetailResponseDTO;
import com.ecommerce.ecommercewebsite.dto.ProductResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public List<ProductResponseDTO> getAllProducts();

    public List<ProductResponseDTO> getProductsByCategoryId(Long id);

    public ProductDetailResponseDTO getProductDetailsById(Long id);

    public List<ProductResponseDTO> filterProductsByPrice(Double minPrice, Double maxPrice);

    public Page<ProductResponseDTO> getAllProductsByDistrict(Long id, int page, int size);
}
