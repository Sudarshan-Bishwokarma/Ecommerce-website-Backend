package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.*;
import com.ecommerce.ecommercewebsite.enums.*;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.exception.UserNotFoundException;
import com.ecommerce.ecommercewebsite.mappers.CategoryMapper;
import com.ecommerce.ecommercewebsite.mappers.ProductMapper;
import com.ecommerce.ecommercewebsite.mappers.VendorMapper;
import com.ecommerce.ecommercewebsite.model.*;
import com.ecommerce.ecommercewebsite.repositories.*;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private BusinessProfileRepository businessProfileRepository;
    @Autowired
    private VendorMapper vendorMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private VendorOrderRepository vendorOrderRepository;

    @Autowired
    private FeaturedPaymentRepository featuredPaymentRepository;


    @Override
    public String deleteVendor(Long id) {
        userRepository.deleteById(id);
        return "Admin Deleted Successfully";
    }


    @Override
    public Long countTotalVendors() {
        Role role = roleRepository.findByRole("ROLE_VENDOR");
        return userRepository.countByRole(role);
    }

    @Override
    public Long countTotalProducts() {
        return productRepository.countByStatus(ProductStatus.ACTIVE);

    }


    @Override
    public Long countTotalUsers() {
        Role role = roleRepository.findByRole("ROLE_USER");
        Long count = userRepository.countByRole(role);
        return count;
    }

    @Override
    public Page<VendorResponseDTO> getAllVendors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Role role = roleRepository.findByRole("ROLE_VENDOR");
        Page<BusinessProfile> businessProfilePage = businessProfileRepository.findAll(pageable);
        return businessProfilePage.map(vendorMapper::map);
    }

    @Override
    public String updateVendorApproval(Long id, ApprovalStatus status) {


        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));

        BusinessProfile businessProfile = businessProfileRepository.findByUser(user).orElseThrow(() -> new ApiException(AuthErrorCode.BUSINESS_PROFILE_NOT_FOUND));

        if (status != ApprovalStatus.APPROVED && status != ApprovalStatus.REJECTED) {
            throw new ApiException(AuthErrorCode.INVALID_STATUS);
        }

        if (businessProfile.getApprovalStatus() != ApprovalStatus.PENDING) {

            throw new ApiException(AuthErrorCode.INVALID_STATUS);
        }

        businessProfile.setApprovalStatus(status);

        businessProfileRepository.save(businessProfile);


        return "Vendor approval updated successfully";
    }

    @Override
    public Page<VendorResponseDTO> getAllPendingVendors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BusinessProfile> profiles = businessProfileRepository.findByApprovalStatus(ApprovalStatus.PENDING, pageable);
        return profiles.map(vendorMapper::map);

    }

    @Override
    public Page<ProductResponseDTO> getPendingProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("productId").descending()); //  admin  see the latest  product
        Page<Product> pendingProducts = productRepository.findByStatus(ProductStatus.APPROVAL_PENDING, pageable);
        return pendingProducts.map(productMapper::mapToDTO);
    }

    @Override
    public String updateApprovalProduct(Long id, ProductStatus status) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
        if (!product.getStatus().equals(ProductStatus.APPROVAL_PENDING)) {
            throw new ApiException(ProductErrorCode.INVALID_PRODUCT_STATUS);
        }
        if (status != ProductStatus.APPROVED && status != ProductStatus.REJECTED) {
            throw new ApiException(ProductErrorCode.INVALID_PRODUCT_STATUS);
        }
        product.setStatus(status);
        productRepository.save(product);
        return "Product approval status updated successfully";
    }


    @Override

    public CategoryResponseDTO addCategory(CategoryRequestDTO categoryRequestDTO) {
        boolean value = categoryRepository.existsByCategoryNameIgnoreCase(categoryRequestDTO.getCategoryName());
        if (value) {
            throw new ApiException(ProductErrorCode.CATEGORY_ALREADY_EXISTS);
        }
        Category category = new Category();
        category.setCategoryName(categoryRequestDTO.getCategoryName());
        if (categoryRequestDTO.getCategoryImage() != null) {
            try {
                category.setCategoryImage(categoryRequestDTO.getCategoryImage().getBytes());
            } catch (IOException e) {
                throw new ApiException(ProductErrorCode.IMAGE_UPLOADED_FAILED);
            }
        }
        Category savedCategory = categoryRepository.save(category);
        CategoryResponseDTO categoryResponseDTO = categoryMapper.mapToDTO(savedCategory);
        return categoryResponseDTO;
    }

    @Override
    public CategoryResponseDTO updateCategory(CategoryUpdateRequestDTO categoryUpdateRequestDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ApiException(ProductErrorCode.CATEGORY_NOT_FOUND)
                );

        if (!category.getCategoryName()
                .equalsIgnoreCase(categoryUpdateRequestDTO.getCategoryName())
                &&
                categoryRepository.existsByCategoryNameIgnoreCase(
                        categoryUpdateRequestDTO.getCategoryName()
                )) {

            throw new ApiException(
                    ProductErrorCode.CATEGORY_ALREADY_EXISTS
            );
        }

        category.setCategoryName(categoryUpdateRequestDTO.getCategoryName());
        if (categoryUpdateRequestDTO.getCategoryImage() != null) {
            try {
                category.setCategoryImage(categoryUpdateRequestDTO.getCategoryImage().getBytes());
            } catch (IOException e) {
                throw new ApiException(ProductErrorCode.IMAGE_UPLOADED_FAILED);
            }
        }
        CategoryResponseDTO categoryResponseDTO = categoryMapper.mapToDTO(categoryRepository.save(category));
        return categoryResponseDTO;
    }

    @Override
    public BigDecimal getOrderCommission() {
        return vendorOrderRepository.getTotalCommission();
    }

    @Override
    public BigDecimal getFeaturedRevenue() {
        return featuredPaymentRepository.getTotalAmountByStatus(PaymentStatus.SUCCESS);
    }

    @Override
    public BigDecimal getTotalEarnings() {
        BigDecimal orderCommission = getOrderCommission();
        BigDecimal featuredRevenue = getFeaturedRevenue();
        return orderCommission.add(featuredRevenue);
    }


}
