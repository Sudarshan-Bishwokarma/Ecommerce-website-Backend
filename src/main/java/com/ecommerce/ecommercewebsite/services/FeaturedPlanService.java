package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.FeaturedPlanRequestDTO;
import com.ecommerce.ecommercewebsite.dto.FeaturedPlanResponseDTO;
import com.ecommerce.ecommercewebsite.dto.VendorFeaturedPlanResponseDTO;
import com.ecommerce.ecommercewebsite.model.FeaturedPlan;

import java.util.List;

public interface FeaturedPlanService {
    FeaturedPlanResponseDTO createPlan(FeaturedPlanRequestDTO requestDTO);

    List<FeaturedPlanResponseDTO> getAllPlans();

    FeaturedPlanResponseDTO updatePlan(FeaturedPlanRequestDTO requestDTO, Long featuredPlanId);

    FeaturedPlanResponseDTO updateStatus(Long featuredPlanId, boolean active);

    //vendor
    List<VendorFeaturedPlanResponseDTO> getActivePlans();
}
