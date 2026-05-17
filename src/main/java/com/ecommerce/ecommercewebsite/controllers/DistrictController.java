package com.ecommerce.ecommercewebsite.controllers;

import com.ecommerce.ecommercewebsite.model.District;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DistrictController {
    @Autowired
    DistrictService districtService;

    // fetch    all  district  of the database
    @GetMapping("/all-districts")
    public ResponseEntity<ApiResponse<List<District>>> getAllDistricts() {
        List<District> districts = districtService.getAllDistricts();
        ApiResponse<List<District>> apiResponse = new ApiResponse<>("Success", districts);
        return ResponseEntity.ok(apiResponse);

    }
}
