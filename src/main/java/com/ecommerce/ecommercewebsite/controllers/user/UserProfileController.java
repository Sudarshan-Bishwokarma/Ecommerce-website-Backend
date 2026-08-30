package com.ecommerce.ecommercewebsite.controllers.user;

import com.ecommerce.ecommercewebsite.dto.ProfileRequestDTO;
import com.ecommerce.ecommercewebsite.dto.ProfileResponseDTO;
import com.ecommerce.ecommercewebsite.dto.users.UserProfileUpdateRequestDTO;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/api/user")
@RestController
public class UserProfileController {
    private final ProfileService profileService;

    public UserProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // get profile
    @GetMapping("/profile")
    ResponseEntity<ApiResponse<ProfileResponseDTO>> getProfile(Principal principal) {
        String email = principal.getName();
        ProfileResponseDTO profileResponseDTO = profileService.getProfile(email);
        ApiResponse<ProfileResponseDTO> apiResponse = new ApiResponse<>("Success", profileResponseDTO);
        return ResponseEntity.ok(apiResponse);
    }

    // update  profile
    @PutMapping(value = "/profile-edit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<ApiResponse<ProfileResponseDTO>> editProfile(Principal principal, @ModelAttribute UserProfileUpdateRequestDTO updateRequestDTO) {
        String email = principal.getName();
        ProfileResponseDTO editResponse = profileService.editProfile(email, updateRequestDTO);
        ApiResponse<ProfileResponseDTO> apiResponse = new ApiResponse<>("Profile Updated Successfully", editResponse);
        return ResponseEntity.ok(apiResponse);
    }

}

