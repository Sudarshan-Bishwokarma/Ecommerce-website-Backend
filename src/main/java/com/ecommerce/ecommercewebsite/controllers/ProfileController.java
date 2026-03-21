package com.ecommerce.ecommercewebsite.controllers;

import com.ecommerce.ecommercewebsite.dto.ProfileResponseDTO;
import com.ecommerce.ecommercewebsite.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class ProfileController {
    @Autowired
    ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponseDTO> getProfile(Principal principal) {
        String email = principal.getName();
        ProfileResponseDTO responseDTO = profileService.getProfile(email);

        return ResponseEntity.ok(responseDTO);
    }

}
