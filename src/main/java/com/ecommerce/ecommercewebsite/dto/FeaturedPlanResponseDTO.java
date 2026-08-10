package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.FeaturePlanType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FeaturedPlanResponseDTO {
    private Long featuredPlanId;

    private String name;

    private Integer durationDays;

    private BigDecimal price;

    private boolean active;
    private FeaturePlanType featurePlanType;

}
