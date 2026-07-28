package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.ProductDetailResponseDTO;
import com.ecommerce.ecommercewebsite.dto.ProductResponseDTO;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.mappers.ProductDetailsMapper;
import com.ecommerce.ecommercewebsite.mappers.ProductMapper;
import com.ecommerce.ecommercewebsite.model.District;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.model.ProductVariant;
import com.ecommerce.ecommercewebsite.repositories.CategoryRepository;
import com.ecommerce.ecommercewebsite.repositories.DistrictRepository;
import com.ecommerce.ecommercewebsite.repositories.ProductRepository;
import com.ecommerce.ecommercewebsite.repositories.ProductVariantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private ProductVariantsRepository productVariantsRepository;
    @Autowired
    private ProductDetailsMapper detailsMapper;
    @Autowired
    private ProductMapper mapper;

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> allProducts = productRepository.findAll();
        if (allProducts.isEmpty()) {
            return new ArrayList<>();
        }
        List<ProductResponseDTO> dtos = new ArrayList<>();
        for (Product product : allProducts) {
            ProductResponseDTO dto = mapper.mapToDTO(product);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<ProductResponseDTO> getProductsByCategoryId(Long id) {
        List<Product> products = productRepository.findByDistrict_id(id);

        List<ProductResponseDTO> dtos = new ArrayList<>();
        for (Product product : products) {
            ProductResponseDTO dto = mapper.mapToDTO(product);
            dtos.add(dto);
        }
        return dtos;

    }

    @Override
    public ProductDetailResponseDTO getProductDetailsById(Long id) {
        Product product = productRepository.findById(id).
                orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
        ProductDetailResponseDTO dto = detailsMapper.mapToDTO(product);

        return dto;
    }

    @Override
    public List<ProductResponseDTO> filterProductsByPrice(Double minPrice, Double maxPrice) {
        List<ProductVariant> allProducts = productVariantsRepository.findByPriceBetween(minPrice, maxPrice);
        if (allProducts.isEmpty()) {
            return new ArrayList<>();
        }
        List<ProductResponseDTO> dtos = new ArrayList<>();
        for (ProductVariant variant : allProducts) {
            ProductResponseDTO responseDTO = mapper.mapToDTO(variant.getProduct());
        }
        return dtos;
    }


    @Override
    public Page<ProductResponseDTO> getAllProductsByDistrict(Long id, int page, int size) {
        // check  district  exist or not
        District district = districtRepository.findById(id).orElseThrow(() -> new ApiException(ProductErrorCode.DISTRICT_NOT_FOUND));
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findByDistrict_id(id, pageable);

        return products.map(mapper::mapToDTO);
    }


}
