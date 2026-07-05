package com.ecommerce.ecommercewebsite.controllers;

import com.ecommerce.ecommercewebsite.dto.ProfileRequestDTO;
import com.ecommerce.ecommercewebsite.dto.ProfileResponseDTO;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class ProfileController {
    @Autowired
    ProfileService profileService;

    // compete profile
    @PostMapping(value = "/complete-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProfileResponseDTO>> competeProfile(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute ProfileRequestDTO profileRequestDTO) {
        String email = userDetails.getUsername();
        ProfileResponseDTO response = profileService.completeProfile(email, profileRequestDTO);
        ApiResponse<ProfileResponseDTO> apiResponse = new ApiResponse<>("Success", response);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


}
