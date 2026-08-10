package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.FeaturedRequestStatus;
import com.ecommerce.ecommercewebsite.enums.ProductStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class VendorProductDetailResponseDTO {
    private Long productId;
    private String productName;
    private String productDescription;
    private String productImageBase64;
    private ProductStatus status;
    private boolean featured;
    private FeaturedRequestStatus featuredRequestStatus;
    private Long featuredRequestId;
    private Long featuredPlanId;
    private String featuredPlanName;
    private Integer featuredDurationDays;
    private Long categoryId;
    private String categoryName;
    private Long districtId;
    private String districtName;

    private Boolean hasVariants;

    private BigDecimal productPrice; // base or min price
    private Integer stock;

    private List<ProductVariantDetailResponseDTO> variantsDetails;
}
