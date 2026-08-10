package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.VendorFeaturedPlanResponseDTO;
import com.ecommerce.ecommercewebsite.model.FeaturedPlan;
import org.springframework.stereotype.Component;

@Component
public class VendorFeaturedPlanMapper {
    public VendorFeaturedPlanResponseDTO mapToDTO(FeaturedPlan featuredPlan) {
        VendorFeaturedPlanResponseDTO vendorFeaturedPlanResponseDTO = new VendorFeaturedPlanResponseDTO();
        vendorFeaturedPlanResponseDTO.setFeaturedPlanId(featuredPlan.getId());
        vendorFeaturedPlanResponseDTO.setName(featuredPlan.getName());
        vendorFeaturedPlanResponseDTO.setPrice(featuredPlan.getPrice());
        vendorFeaturedPlanResponseDTO.setDurationDays(featuredPlan.getDurationDays());
        vendorFeaturedPlanResponseDTO.setFeaturePlanType(featuredPlan.getFeaturePlanType());
        return vendorFeaturedPlanResponseDTO;
    }
}   
