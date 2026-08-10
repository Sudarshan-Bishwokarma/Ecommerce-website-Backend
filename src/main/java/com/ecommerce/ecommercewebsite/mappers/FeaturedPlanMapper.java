package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.FeaturedPlanResponseDTO;
import com.ecommerce.ecommercewebsite.model.FeaturedPlan;
import org.springframework.stereotype.Component;

@Component
public class FeaturedPlanMapper {
    public FeaturedPlanResponseDTO mapToDTO(FeaturedPlan featuredPlan) {
        FeaturedPlanResponseDTO featuredPlanResponseDTO = new FeaturedPlanResponseDTO();
        featuredPlanResponseDTO.setFeaturedPlanId(featuredPlan.getId());
        featuredPlanResponseDTO.setName(featuredPlan.getName());
        featuredPlanResponseDTO.setDurationDays(featuredPlan.getDurationDays());
        featuredPlanResponseDTO.setPrice(featuredPlan.getPrice());
        featuredPlanResponseDTO.setActive(featuredPlan.isActive());
        featuredPlanResponseDTO.setFeaturePlanType(featuredPlan.getFeaturePlanType());
        return featuredPlanResponseDTO;

    }
}
