package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.VendorProductDetailResponseDTO;
import com.ecommerce.ecommercewebsite.dto.ProductResponseDTO;
import com.ecommerce.ecommercewebsite.dto.users.ProductDetailResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {

    public Page<ProductResponseDTO> getAllProducts(int page, int size);

    public List<ProductResponseDTO> getProductsByCategoryId(Long id);

    public List<ProductResponseDTO> filterProductsByPrice(Double minPrice, Double maxPrice);

    public Page<ProductResponseDTO> getAllProductsByDistrict(Long id, int page, int size);

    public ProductDetailResponseDTO getProductDetails(Long id);
}
