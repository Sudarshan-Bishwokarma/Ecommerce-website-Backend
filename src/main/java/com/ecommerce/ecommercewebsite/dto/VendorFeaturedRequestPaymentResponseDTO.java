package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.FeaturedRequestStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class VendorFeaturedRequestPaymentResponseDTO {
    private Long featuredRequestId;

    private Long featuredPlanId;

    private String productName;

    private String featuredPlanName;

    private Integer durationDays;

    private BigDecimal price;

    private FeaturedRequestStatus status;
}
