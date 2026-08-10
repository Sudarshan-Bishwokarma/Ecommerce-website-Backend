package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.FeaturePlanType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FeaturedPlanRequestDTO {
    private String name;

    private Integer durationDays;

    private BigDecimal price;
    private FeaturePlanType featurePlanType;
}
