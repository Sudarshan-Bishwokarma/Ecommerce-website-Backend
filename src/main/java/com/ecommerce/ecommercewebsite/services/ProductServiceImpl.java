package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.ProductResponseDTO;
import com.ecommerce.ecommercewebsite.dto.users.ProductDetailResponseDTO;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.mappers.ProductDetailMapper;
import com.ecommerce.ecommercewebsite.mappers.VendorProductDetailsMapper;
import com.ecommerce.ecommercewebsite.mappers.ProductMapper;
import com.ecommerce.ecommercewebsite.model.District;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.model.ProductVariant;
import com.ecommerce.ecommercewebsite.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private VendorProductDetailsMapper detailsMapper;
    @Autowired
    private ProductMapper mapper;
    @Autowired
    private FeaturedRequestRepository featuredRequestRepository;
    @Autowired
    private ProductDetailMapper productDetailMapper;

    @Override
    public Page<ProductResponseDTO> getProducts(Long districtId, Long categoryId, String search, String sortType, int page, int size) {
        Pageable pageable;
        if (sortType != null) {
            switch (sortType) {
                case "priceAsc":
                    pageable = PageRequest.of(page, size, Sort.by("displayPrice").ascending());
                    break;
                case "priceDesc":
                    pageable = PageRequest.of(page, size, Sort.by("displayPrice").descending());
                    break;
                case "nameAsc":
                    pageable = PageRequest.of(page, size, Sort.by("productName").ascending());
                    break;
                case "nameDesc":
                    pageable = PageRequest.of(page, size, Sort.by("productName").descending());
                    break;
                default:
                    pageable = PageRequest.of(page, size);
            }

        } else {
            pageable = PageRequest.of(page, size);
        }
        Page<Product> allProducts;
        if (districtId != null && categoryId != null && search != null) {
            allProducts = productRepository.findByDistrict_IdAndCategory_IdAndProductNameContainingIgnoreCase(districtId, categoryId, search, pageable);
        } else if (districtId != null && categoryId != null) {
            allProducts = productRepository.findByDistrict_IdAndCategory_Id(districtId, categoryId, pageable);
        } else if (districtId != null && search != null) {
            allProducts = productRepository.findByDistrict_IdAndProductNameContainingIgnoreCase(districtId, search, pageable);
        } else if (categoryId != null && search != null) {
            allProducts = productRepository.findByCategory_IdAndProductNameContainingIgnoreCase(categoryId, search, pageable);
        } else if (districtId != null) {
            allProducts = productRepository.findByDistrict_id(districtId, pageable);
        } else if (categoryId != null) {
            allProducts = productRepository.findByCategory_id(categoryId, pageable);
        } else if (search != null && !search.trim().isEmpty()) {
            allProducts = productRepository.findByProductNameContainingIgnoreCase(search, pageable);
        } else {
            allProducts = productRepository.findAll(pageable);
        }

        return allProducts.map(mapper::mapToDTO);
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

    @Override
    public ProductDetailResponseDTO getProductDetails(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
        ProductDetailResponseDTO responseDTO = productDetailMapper.mapToDTO(product);
        return responseDTO;

    }


}
