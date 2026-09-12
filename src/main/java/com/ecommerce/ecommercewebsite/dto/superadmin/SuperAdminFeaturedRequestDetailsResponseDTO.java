package com.ecommerce.ecommercewebsite.dto.superadmin;

import com.ecommerce.ecommercewebsite.enums.FeaturePlanType;
import com.ecommerce.ecommercewebsite.enums.FeaturedRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuperAdminFeaturedRequestDetailsResponseDTO {
    private Long requestId;

    // Product
    private Long productId;
    private String productName;
    private String productImage;
    private String productDescription;
    private boolean hasVariants;

    // Vendor
    private Long vendorId;
    private String vendorName;
    private String vendorEmail;
    private String vendorPhone;

    // Business
    private String businessName;
    private String businessEmail;
    private String businessPhone;
    private String businessAddress;
    private String businessWebsite;


    // Classification
    private String categoryName;
    private String districtName;

    // Featured Plan
    private Long featuredPlanId;
    private String featuredPlanName;
    private Integer durationDays;
    private BigDecimal price;
    private FeaturePlanType featurePlanType;

    // Request
    private FeaturedRequestStatus status;
    private String adminMessage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String transactionUuid;
}
