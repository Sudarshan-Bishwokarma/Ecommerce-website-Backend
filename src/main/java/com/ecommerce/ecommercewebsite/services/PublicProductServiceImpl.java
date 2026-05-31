package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.ProductResponseDTO;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.exception.ProductNotFoundException;
import com.ecommerce.ecommercewebsite.model.District;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.repositories.CategoryRepository;
import com.ecommerce.ecommercewebsite.repositories.DistrictRepository;
import com.ecommerce.ecommercewebsite.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class PublicProductServiceImpl implements PublicProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private DistrictRepository districtRepository;

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        if (allProducts.isEmpty()) {
            return new ArrayList<>();
        }
        List<ProductResponseDTO> dtos = new ArrayList<>();
        for (Product product : allProducts) {
            ProductResponseDTO dto = mapToDTO(product);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategoryId(Long id) {
        List<Product> products = productRepository.findByCategory_id(id);

        List<ProductResponseDTO> dtos = new ArrayList<>();
        for (Product product : products) {
            ProductResponseDTO dto = mapToDTO(product);
            dtos.add(dto);
        }
        return dtos;

    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
        ProductResponseDTO dto = mapToDTO(product);

        return dto;
    }

    @Override
    public List<ProductResponseDTO> filterProductsByPrice(Double minPrice, Double maxPrice) {
        List<Product> allProducts = productRepository.findByProductPriceBetween(minPrice, maxPrice);
        if (allProducts.isEmpty()) {
            throw new ProductNotFoundException("Product Not Found");
        }
        List<ProductResponseDTO> dtos = new ArrayList<>();
        for (Product product : allProducts) {
            ProductResponseDTO dto = mapToDTO(product);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public Page<ProductResponseDTO> sortProducts(String sortType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> allProducts;
        switch (sortType) {
            case "priceAsc":
                allProducts = productRepository.findAllByOrderByProductPriceAsc(pageable);
                break;
            case "priceDesc":
                allProducts = productRepository.findAllByOrderByProductPriceDesc(pageable);
                break;
            case "nameAsc":
                allProducts = productRepository.findAllByOrderByProductNameAsc(pageable);
                break;
            case "nameDesc":
                allProducts = productRepository.findAllByOrderByProductNameDesc(pageable);
                break;
            default:
                allProducts = productRepository.findAll(pageable);

        }
        return allProducts.map(this::mapToDTO);
    }

    @Override
    public Page<ProductResponseDTO> getAllProductsByDistrict(Long id, int page, int size) {
        // check  district  exist or not
        District district = districtRepository.findById(id).orElseThrow(() -> new ApiException(ProductErrorCode.DISTRICT_NOT_FOUND));
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findByDistrict_Id(id, pageable);

        return products.map(this::mapToDTO);
    }

    // ------------------------Helper Class ------------------------
    private ProductResponseDTO mapToDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductDescription(product.getProductDescription());
        dto.setProductPrice(product.getProductPrice());
        dto.setProductCategory(product.getCategory().getCategoryName());
        dto.setDistrictName(product.getDistrict().getDistrictName());
        if (product.getProductImage() != null) {
            String base64 = Base64.getEncoder().encodeToString(product.getProductImage());
            dto.setProductImageBase64(base64);
        }
        return dto;
    }
}
