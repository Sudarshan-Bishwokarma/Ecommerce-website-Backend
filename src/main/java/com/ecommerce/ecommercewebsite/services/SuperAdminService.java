package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.*;
import com.ecommerce.ecommercewebsite.dto.superadmin.*;
import com.ecommerce.ecommercewebsite.dto.vendor.VendorDetailsResponseDTO;
import com.ecommerce.ecommercewebsite.enums.ApprovalStatus;
import com.ecommerce.ecommercewebsite.enums.ProductStatus;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SuperAdminService {
    public String deleteVendor(Long id);

    public Long countTotalUsers();

    public Long countTotalVendors();

    public Long countTotalProducts();

    public VendorDetailsResponseDTO getVendorDetails(Long id);

    public Page<VendorResponseDTO> getAllVendors(int page, int size);

    public String updateVendorApproval(Long id, ApprovalStatus status);

    public Page<VendorResponseDTO> getAllPendingVendors(int page, int size);

    Page<ProductResponseDTO> getProducts(ProductStatus status, int page, int size);

    public ProductDetailsResponseDTO getProductDetails(Long id);

    public Page<ProductResponseDTO> getPendingProducts(int page, int size);

    public String updateApprovalProduct(Long id, ProductStatus status);


    public BigDecimal getOrderCommission();

    public BigDecimal getFeaturedRevenue();

    public BigDecimal getTotalEarnings();

    public SuperAdminIncomeResponseDTO getIncome(
            LocalDate startDate,
            LocalDate endDate
    );

    List<SuperAdminSalesDTO> getSalesByProduct(String sort);

    List<SuperAdminCommissionDTO> getCommissionByVendor(LocalDate startDate, LocalDate endDate);

    List<SuperAdminDistrictCommissionDTO> getCommissionByDistrict(LocalDate startDate, LocalDate endDate);
}
