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
import com.ecommerce.ecommercewebsite.repositories.FeaturedRequestRepository;
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
    @Autowired
    private FeaturedRequestRepository featuredRequestRepository;

    @Override
    public FeaturedPlanResponseDTO createPlan(FeaturedPlanRequestDTO requestDTO) {
        FeaturedPlan featuredPlan = new FeaturedPlan();

        featuredPlan.setName(requestDTO.getName());
        featuredPlan.setDurationDays(requestDTO.getDurationDays());
        featuredPlan.setPrice(requestDTO.getPrice());
        featuredPlan.setFeaturePlanType(requestDTO.getFeaturePlanType());
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
        boolean exists = featuredRequestRepository.existsByFeaturedPlanId(featuredPlanId);

        if (exists) {
            throw new ApiException(FeaturedPlanErrorCode.FEATURED_PLAN_ALREADY_IN_USE);
        }
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

    @Override
    public FeaturedPlanResponseDTO getPlanById(Long id) {
        FeaturedPlan plan = featuredPlanRepository.findById(id).orElseThrow(() -> new ApiException(FeaturedPlanErrorCode.FEATURED_PLAN_NOT_FOUND));
        FeaturedPlanResponseDTO responseDTO = featuredPlanMapper.mapToDTO(plan);
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

    public void deletePlan(Long id) {
        if (featuredRequestRepository.existsByFeaturedPlanId(id)) {
            throw new ApiException(FeaturedPlanErrorCode.FEATURED_PLAN_IN_USE);
        }

        FeaturedPlan plan = featuredPlanRepository.findById(id)
                .orElseThrow(() -> new ApiException(FeaturedPlanErrorCode.FEATURED_PLAN_NOT_FOUND));

        featuredPlanRepository.delete(plan);
    }
}
