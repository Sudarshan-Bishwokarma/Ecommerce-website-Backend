package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.ProductRequestDTO;
import com.ecommerce.ecommercewebsite.dto.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    // admin only methods
    public ProductResponseDTO addProduct(ProductRequestDTO productRequestDTO, MultipartFile file);

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO);

    public String deleteProduct(Long id);

    // admin + user  methods
    public List<ProductResponseDTO> getAllProducts();

    public List<ProductResponseDTO> getProductsByCategoryId(Long id);

    public ProductResponseDTO getProductById(Long id);

    public List<ProductResponseDTO> filterProductsByPrice(Double minPrice, Double maxPrice);

    public Page<ProductResponseDTO> sortProducts(String sortType, int page, int size);

    public Page<ProductResponseDTO> getAllProductsByDistrict(Long id, int page, int size);

}
