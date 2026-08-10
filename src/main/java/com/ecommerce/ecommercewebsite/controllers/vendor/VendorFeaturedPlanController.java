package com.ecommerce.ecommercewebsite.controllers.vendor;

import com.ecommerce.ecommercewebsite.dto.FeaturedPlanResponseDTO;
import com.ecommerce.ecommercewebsite.dto.FeaturedRequestVendorResponseDTO;
import com.ecommerce.ecommercewebsite.dto.VendorFeaturedPlanResponseDTO;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.FeaturedPlanService;
import com.ecommerce.ecommercewebsite.services.FeaturedRequestVendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/vendor")
public class VendorFeaturedPlanController {
    @Autowired
    private FeaturedPlanService featuredPlanService;

    //  get active  featured plans
    @GetMapping("/feature-plans")
    public ResponseEntity<ApiResponse<List<VendorFeaturedPlanResponseDTO>>> getActiveFeaturedPlans() {
        List<VendorFeaturedPlanResponseDTO> plans = featuredPlanService.getActivePlans();

        ApiResponse<List<VendorFeaturedPlanResponseDTO>> apiResponse = new ApiResponse<>("Available Featured Plans", plans);
        return ResponseEntity.ok(apiResponse);
    }

}

