package com.ecommerce.ecommercewebsite.model;

import com.ecommerce.ecommercewebsite.enums.FeaturePlanType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FeaturedPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private Integer durationDays;

    private BigDecimal price;

    private boolean active;
    @Enumerated(EnumType.STRING)
    private FeaturePlanType featurePlanType;
}
