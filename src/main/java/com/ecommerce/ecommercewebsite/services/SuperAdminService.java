package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.*;
import com.ecommerce.ecommercewebsite.enums.ApprovalStatus;
import com.ecommerce.ecommercewebsite.enums.ProductStatus;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface SuperAdminService {
    public String deleteVendor(Long id);

    public Long countTotalUsers();

    public Long countTotalVendors();

    public Long countTotalProducts();


    public Page<VendorResponseDTO> getAllVendors(int page, int size);

    public String updateVendorApproval(Long id, ApprovalStatus status);

    public Page<VendorResponseDTO> getAllPendingVendors(int page, int size);

    public Page<ProductResponseDTO> getPendingProducts(int page, int size);

    public String updateApprovalProduct(Long id, ProductStatus status);

    public CategoryResponseDTO addCategory(CategoryRequestDTO categoryRequestDTO);

    public CategoryResponseDTO updateCategory(CategoryUpdateRequestDTO categoryUpdateRequestDTO, Long categoryId);

    public BigDecimal getOrderCommission();

    public BigDecimal getFeaturedRevenue();

    public BigDecimal getTotalEarnings();
}
