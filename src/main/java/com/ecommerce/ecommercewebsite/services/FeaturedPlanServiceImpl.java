package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.FeaturedPlanRequestDTO;
import com.ecommerce.ecommercewebsite.dto.FeaturedPlanResponseDTO;
import com.ecommerce.ecommercewebsite.dto.VendorFeaturedPlanResponseDTO;
import com.ecommerce.ecommercewebsite.enums.FeaturedPlanErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.mappers.FeaturedPlanMapper;
import com.ecommerce.ecommercewebsite.mappers.VendorFeaturedPlanMapper;
import com.ecommerce.ecommercewebsite.model.FeaturedPlan;
import com.ecommerce.ecommercewebsite.repositories.FeaturedPlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeaturedPlanServiceImpl implements FeaturedPlanService {
    @Autowired
    private FeaturedPlanRepository featuredPlanRepository;

    @Autowired
    private FeaturedPlanMapper featuredPlanMapper;
    @Autowired
    private VendorFeaturedPlanMapper vendorFeaturedPlanMapper;

    @Override
    public FeaturedPlanResponseDTO createPlan(FeaturedPlanRequestDTO requestDTO) {
        FeaturedPlan featuredPlan = new FeaturedPlan();

        featuredPlan.setName(requestDTO.getName());
        featuredPlan.setDurationDays(requestDTO.getDurationDays());
        featuredPlan.setPrice(requestDTO.getPrice());
        featuredPlan.setActive(true);
        FeaturedPlan savedPlan = featuredPlanRepository.save(featuredPlan);
        FeaturedPlanResponseDTO responseDTO = featuredPlanMapper.mapToDTO(savedPlan);
        return responseDTO;
    }

    @Override
    public List<FeaturedPlanResponseDTO> getAllPlans() {
        List<FeaturedPlan> allFeaturedPlans = featuredPlanRepository.findAll();

        List<FeaturedPlanResponseDTO> responseDTOs = new ArrayList<>();
        for (FeaturedPlan featuredPlan : allFeaturedPlans) {
            responseDTOs.add(featuredPlanMapper.mapToDTO(featuredPlan));
        }
        return responseDTOs;
    }

    @Override
    public FeaturedPlanResponseDTO updatePlan(FeaturedPlanRequestDTO requestDTO, Long featuredPlanId) {
        FeaturedPlan plan = featuredPlanRepository.findById(featuredPlanId).orElseThrow(() -> new ApiException(FeaturedPlanErrorCode.FEATURED_PLAN_NOT_FOUND));
        plan.setName(requestDTO.getName());
        plan.setPrice(requestDTO.getPrice());
        plan.setDurationDays(requestDTO.getDurationDays());
        plan.setFeaturePlanType(requestDTO.getFeaturePlanType());
        FeaturedPlan updatedPLan = featuredPlanRepository.save(plan);
        FeaturedPlanResponseDTO responseDTO = featuredPlanMapper.mapToDTO(updatedPLan);
        return responseDTO;
    }

    @Override
    public FeaturedPlanResponseDTO updateStatus(Long featuredPlanId, boolean active) {
        FeaturedPlan plan = featuredPlanRepository.findById(featuredPlanId).orElseThrow(() -> new ApiException(FeaturedPlanErrorCode.FEATURED_PLAN_NOT_FOUND));
        if (plan.isActive() == active) {
            throw new ApiException(FeaturedPlanErrorCode.INVALID_FEATURED_STATUS);
        }
        plan.setActive(active);
        FeaturedPlan savedPlan = featuredPlanRepository.save(plan);
        FeaturedPlanResponseDTO responseDTO = featuredPlanMapper.mapToDTO(savedPlan);
        return responseDTO;
    }

    // for vendor
    @Override
    public List<VendorFeaturedPlanResponseDTO> getActivePlans() {
        List<FeaturedPlan> allFeaturedPlans = featuredPlanRepository.findByActiveTrue();
        List<VendorFeaturedPlanResponseDTO> responseDTOs = new ArrayList<>();
        for (FeaturedPlan plan : allFeaturedPlans) {
            responseDTOs.add(vendorFeaturedPlanMapper.mapToDTO(plan));
        }
        return responseDTOs;
    }
}
