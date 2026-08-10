package com.ecommerce.ecommercewebsite.controllers.superAdmin;

import com.ecommerce.ecommercewebsite.dto.FeaturedPlanRequestDTO;
import com.ecommerce.ecommercewebsite.dto.FeaturedPlanResponseDTO;
import com.ecommerce.ecommercewebsite.dto.FeaturedProductResponseDTO;
import com.ecommerce.ecommercewebsite.model.FeaturedPlan;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.FeaturedPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/super-admin")
public class FeaturedPlanAdminController {
    @Autowired
    private FeaturedPlanService featuredPlanService;

    // create featured plan
    @PostMapping("/featured-plans")
    public ResponseEntity<ApiResponse<FeaturedPlanResponseDTO>> createFeaturedPlan(@RequestBody FeaturedPlanRequestDTO requestDTO) {
        FeaturedPlanResponseDTO response = featuredPlanService.createPlan(requestDTO);
        ApiResponse<FeaturedPlanResponseDTO> apiResponse = new ApiResponse<>("Featured Plan Created Successfully", response);
        return ResponseEntity.ok(apiResponse);
    }

    // get all  featured plan
    @GetMapping("/featured-plans")
    public ResponseEntity<ApiResponse<List<FeaturedPlanResponseDTO>>> getAllFeaturedPlans() {
        List<FeaturedPlanResponseDTO> allFeaturedPlans = featuredPlanService.getAllPlans();
        ApiResponse<List<FeaturedPlanResponseDTO>> apiResponse = new ApiResponse<>("All Featured Plans", allFeaturedPlans);
        return ResponseEntity.ok(apiResponse);
    }

    // update featured plan
    @PutMapping("/featured-plans/{id}/update")
    public ResponseEntity<ApiResponse<FeaturedPlanResponseDTO>> updateFeaturePlan(
            @RequestBody FeaturedPlanRequestDTO featuredPlanRequestDTO, @PathVariable Long id
    ) {
        FeaturedPlanResponseDTO updatedResponse = featuredPlanService.updatePlan(featuredPlanRequestDTO, id);
        ApiResponse<FeaturedPlanResponseDTO> apiResponse = new ApiResponse<>("All Featured Plans", updatedResponse);
        return ResponseEntity.ok(apiResponse);
    }

    //  update  status
    @PatchMapping("/featured-plans/{id}/status")
    public ResponseEntity<ApiResponse<FeaturedPlanResponseDTO>> updateFeaturedPlanStatus(
            @PathVariable Long id,
            @RequestParam boolean active
    ) {
        FeaturedPlanResponseDTO response = featuredPlanService.updateStatus(id, active);
        ApiResponse<FeaturedPlanResponseDTO> apiResponse = new ApiResponse<>("Featured Plan Status Updated Successfully", response);
        return ResponseEntity.ok(apiResponse);
    }


}
