package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.*;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.enums.ProductStatus;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.mappers.ProductMapper;
import com.ecommerce.ecommercewebsite.mappers.VendorMapper;
import com.ecommerce.ecommercewebsite.model.*;
import com.ecommerce.ecommercewebsite.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class VendorProductServiceImpl implements VendorProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    private ProductVariantsRepository productVariantsRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductMapper mapper;
    @Autowired
    private VendorMapper vendorMapper;

    @Override
    public ProductResponseDTO addProduct(String email, ProductRequestDTO request) {

        User vendor = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));

        District district = districtRepository.findById(request.getDistrictId())
                .orElseThrow(() -> new ApiException(ProductErrorCode.DISTRICT_NOT_FOUND));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ApiException(ProductErrorCode.CATEGORY_NOT_FOUND));

        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setProductDescription(request.getProductDescription());
        product.setStatus(ProductStatus.DRAFT);
        product.setVendor(vendor);
        product.setCategory(category);
        product.setDistrict(district);

        try {
            product.setProductImage(request.getProductImage().getBytes());
        } catch (Exception e) {
            throw new ApiException(ProductErrorCode.PRODUCT_IMAGE_NOT_FOUND);
        }

        boolean hasVariants =
                request.getVariants() != null && !request.getVariants().isEmpty();

        product.setHasVariants(hasVariants);
        // CASE 1: SIMPLE PRODUCT

        if (!hasVariants) {

            product.setPrice(request.getPrice());
            product.setStock(request.getStock());
        }

        Product savedProduct = productRepository.save(product);


        // CASE 2: VARIANT PRODUCT
        if (hasVariants) {

            List<ProductVariants> variants = new ArrayList<>();

            for (ProductVariantRequestDTO dto : request.getVariants()) {

                ProductVariants v = new ProductVariants();
                v.setProduct(savedProduct);
                v.setSize(dto.getSize());
                v.setColor(dto.getColor());
                v.setPrice(dto.getPrice());
                v.setStock(dto.getStock());

                variants.add(v);
            }

            productVariantsRepository.saveAll(variants);
            savedProduct.getProductVariants().addAll(variants);
        }

        return mapper.mapToDTO(savedProduct);
    }

    @Override
    public String updateStatus(Long id, ProductStatus status) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
        product.setStatus(status);
        productRepository.save(product);
        return "Success";
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, ProductRequestUpdateDTO productRequestUpdateDTO) {
        // check if the   product exist in the database
        Product product = productRepository.findById(id).orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
        product.setProductName(productRequestUpdateDTO.getProductName());
        product.setProductDescription(productRequestUpdateDTO.getProductDescription());
        product.setStatus(ProductStatus.DRAFT);
        Category category = categoryRepository.findById(productRequestUpdateDTO.getCategoryId())
                .orElseThrow(() -> new ApiException(ProductErrorCode.CATEGORY_NOT_FOUND));
        product.setCategory(category);
        District district = districtRepository.findById(productRequestUpdateDTO.getDistrictId()).orElseThrow(() -> new ApiException(ProductErrorCode.DISTRICT_NOT_FOUND));
        product.setDistrict(district);
        Product savedProduct = productRepository.save(product);

        for (ProductVariantUpdateRequestDTO dto : productRequestUpdateDTO.getVariants()) {
            ProductVariants variant = productVariantsRepository.findById(dto.getVariantId())
                    .orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_VARIANTS_NOT_FOUND));

            variant.setSize(dto.getSize());
            variant.setColor(dto.getColor());
            variant.setPrice(dto.getPrice());
            variant.setStock(dto.getStock());

            productVariantsRepository.save(variant);
        }
        System.out.println("Product Updated Successfully");

        //  prepared for response
        ProductResponseDTO responseDTO = mapper.mapToDTO(savedProduct);

        return responseDTO;
    }

    @Override
    public String deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
        productRepository.delete(product);
        return "Product Deleted Successfully";
    }

    @Override
    public Page<ProductResponseDTO> getMyProducts(String email, int page, int size) {
        // fetch the user   first
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        Long vendor_id = user.getId();
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> myProducts = productRepository.findByVendor_Id(vendor_id, pageable);
        if (!myProducts.hasContent()) {
            throw new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND);
        }
        return myProducts.map(mapper::mapToDTO);

    }


    @Override
    public Page<ProductResponseDTO> getProducts(String email, Long districtId, Long categoryId, String sortType, int page, int size) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        Long vendor_id = user.getId();
        Pageable pageable;
        if (sortType != null) {
            switch (sortType) {
                case "priceAsc":
                    pageable = PageRequest.of(page, size, Sort.by("productPrice").ascending());
                    break;
                case "priceDesc":
                    pageable = PageRequest.of(page, size, Sort.by("productPrice").descending());
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
        if (districtId != null && categoryId != null) {
            allProducts = productRepository.findByVendor_IdAndDistrict_IdAndCategory_Id(vendor_id, districtId, categoryId, pageable);

        } else if (districtId != null) {
            allProducts = productRepository.findByVendor_IdAndDistrict_Id(vendor_id, districtId, pageable);
        } else if (categoryId != null) {
            allProducts = productRepository.findByVendor_IdAndCategory_Id(vendor_id, categoryId, pageable);
        } else {
            allProducts = productRepository.findByVendor_Id(vendor_id, pageable);
        }
        return allProducts.map(mapper::mapToDTO);
    }
}