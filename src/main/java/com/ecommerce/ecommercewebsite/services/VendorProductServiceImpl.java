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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

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
    public ProductResponseDTO addProduct(
            String email,
            ProductRequestDTO request,
            MultipartFile productImage,
            List<MultipartFile> variantImages
    ) {

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

        // main image
        try {
            if (productImage != null) {
                product.setProductImage(productImage.getBytes());
            }
        } catch (Exception e) {
            throw new ApiException(ProductErrorCode.PRODUCT_IMAGE_NOT_FOUND);
        }

        boolean hasVariants =
                request.getVariants() != null && !request.getVariants().isEmpty();

        product.setHasVariants(hasVariants);

        // SIMPLE PRODUCT
        if (!hasVariants) {
            product.setPrice(request.getPrice());
            product.setStock(request.getStock());
        }

        Product savedProduct = productRepository.save(product);

        //  if   i has  variant
        if (hasVariants) {

            List<ProductVariants> variants = new ArrayList<>();

            for (int i = 0; i < request.getVariants().size(); i++) {

                ProductVariantRequestDTO dto = request.getVariants().get(i);

                ProductVariants v = new ProductVariants();
                v.setProduct(savedProduct);
                v.setSize(dto.getSize());
                v.setColor(dto.getColor());
                v.setPrice(dto.getPrice());
                v.setStock(dto.getStock());

                // VARIANT IMAGE
                if (variantImages != null && i < variantImages.size()) {
                    try {
                        v.setImage(variantImages.get(i).getBytes());
                    } catch (Exception e) {
                        throw new ApiException(ProductErrorCode.PRODUCT_IMAGE_NOT_FOUND);
                    }
                }

                variants.add(v);
            }

            productVariantsRepository.saveAll(variants);
            savedProduct.setProductVariants(variants);
        }

        return mapper.mapToDTO(savedProduct);
    }

    @Override
    public String updateStatus(Long id, ProductStatusUpdateRequest request) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
        product.setStatus(request.getStatus());
        productRepository.save(product);
        return "Success";
    }

    @Override
    public ProductResponseDTO updateProduct(Long id, String email, ProductUpdateRequestDTO request, MultipartFile productImage, Map<String, MultipartFile> variantImages) {
        // check if the   product exist in the database
        Product product = productRepository.findById(id).orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
        product.setProductName(request.getProductName());
        product.setProductDescription(request.getProductDescription());
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ApiException(ProductErrorCode.CATEGORY_NOT_FOUND));
        product.setCategory(category);
        District district = districtRepository.findById(request.getDistrictId()).orElseThrow(() -> new ApiException(ProductErrorCode.DISTRICT_NOT_FOUND));
        product.setDistrict(district);
        // Verify vendor owns product
        if (!product.getVendor().getEmail().equals(email)) {
            throw new ApiException(ProductErrorCode.UNAUTHORIZED_PRODUCT_ACCESS);
        }
        if (productImage != null && !productImage.isEmpty()) {

            try {

                product.setProductImage(
                        productImage.getBytes()
                );

            } catch (IOException e) {

                throw new ApiException(
                        ProductErrorCode.PRODUCT_IMAGE_NOT_FOUND
                );

            }
        }
        // simple product
        if (Boolean.FALSE.equals(request.getHasVariants())) {

            product.setHasVariants(false);
            product.setPrice(request.getPrice());
            product.setStock(request.getStock());

        }
        // if product has  variant
        if (request.getHasVariants()) {
            product.setHasVariants(true);

            product.setPrice(null);
            product.setStock(null);
            List<ProductVariantUpdateDTO> variantList = request.getVariants();
            for (ProductVariantUpdateDTO v : variantList) {
                if (v.getVariantId() != null) {
                    ProductVariants existingVariant = productVariantsRepository.findById(v.getVariantId()).orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_VARIANTS_NOT_FOUND));
                    existingVariant.setSize(v.getSize());
                    existingVariant.setColor(v.getColor());
                    existingVariant.setPrice(v.getPrice());
                    existingVariant.setStock(v.getStock());
                    MultipartFile image = variantImages.get("variantImage_" + v.getVariantId());
                    if (image != null && !image.isEmpty()) {
                        try {
                            existingVariant.setImage(image.getBytes());
                        } catch (IOException e) {
                            throw new ApiException(ProductErrorCode.PRODUCT_IMAGE_NOT_FOUND);
                        }
                    }
                } else {
                    ProductVariants newVariant = new ProductVariants();
                    newVariant.setSize(v.getSize());
                    newVariant.setColor(v.getColor());
                    newVariant.setPrice(v.getPrice());
                    newVariant.setStock(v.getStock());
                    newVariant.setProduct(product);
                    MultipartFile image = variantImages.get("newVariantImage_" + variantList.indexOf(v));


                    if (image != null && !image.isEmpty()) {
                        try {
                            newVariant.setImage(image.getBytes());
                        } catch (IOException e) {
                            throw new ApiException(ProductErrorCode.PRODUCT_IMAGE_NOT_FOUND);
                        }
                    }
                    product.getProductVariants().add(newVariant);

                }
            }

        }
        //  delete variant
        if (request.getDeletedVariantIds() != null && !request.getDeletedVariantIds().isEmpty()) {
            for (Long variantId : request.getDeletedVariantIds()) {

                ProductVariants variant =
                        productVariantsRepository.findById(variantId)
                                .orElseThrow(() ->
                                        new ApiException(ProductErrorCode.PRODUCT_VARIANTS_NOT_FOUND)
                                );


                // check variant belongs to this product
                if (!variant.getProduct().getProductId().equals(id)) {

                    throw new ApiException(
                            ProductErrorCode.UNAUTHORIZED_PRODUCT_ACCESS
                    );
                }

                productVariantsRepository.delete(variant);
            }
        }
        Product savedProduct = productRepository.save(product);

        ProductResponseDTO dto = mapper.mapToDTO(savedProduct);
        return dto;
    }

    @Override
    public String deleteProduct(Long id, String email) {
        Product product = productRepository.findByProductIdAndVendor_Email(id, email).orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
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
    public Page<ProductResponseDTO> getProducts(String email, Long districtId, Long categoryId, String sortType, String search, int page, int size) {
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
        if (districtId != null && categoryId != null && search != null) {
            allProducts = productRepository.findByVendor_IdAndDistrict_IdAndCategory_IdAndProductNameContainingIgnoreCase(vendor_id, districtId, categoryId, search, pageable);
        } else if (districtId != null && categoryId != null) {
            allProducts = productRepository.findByVendor_IdAndCategory_IdAndDistrict_Id(vendor_id, categoryId, districtId, pageable);
        } else if (search != null && categoryId != null) {
            allProducts = productRepository.findByVendor_IdAndCategory_IdAndProductNameContainingIgnoreCase(vendor_id, categoryId, search, pageable);
        } else if (search != null && districtId != null) {
            allProducts = productRepository.findByVendor_IdAndDistrict_IdAndProductNameContainingIgnoreCase(vendor_id, districtId, search, pageable);
        } else if (districtId != null) {
            allProducts = productRepository.findByVendor_IdAndDistrict_Id(vendor_id, districtId, pageable);
        } else if (categoryId != null) {
            allProducts = productRepository.findByVendor_IdAndCategory_Id(vendor_id, categoryId, pageable);
        } else if (search != null) {
            allProducts = productRepository.findByVendor_IdAndProductNameContainingIgnoreCase(vendor_id, search, pageable);
        } else {
            allProducts = productRepository.findByVendor_Id(vendor_id, pageable);
        }
        return allProducts.map(mapper::mapToDTO);
    }
}