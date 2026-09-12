package com.ecommerce.ecommercewebsite.services.superadmin;

import com.ecommerce.ecommercewebsite.dto.superadmin.SuperAdminProfileResponseDTO;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.model.Profile;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class SuperAdminProfileServiceImpl implements SuperAdminProfileService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public SuperAdminProfileResponseDTO getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));

        // Get profile
        Profile profile = user.getProfile();

        // Create response DTO
        SuperAdminProfileResponseDTO dto = new SuperAdminProfileResponseDTO();

        // User information
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());

        // Role
        if (user.getRole() != null) {
            dto.setRole(user.getRole().getRole());
        }

        // Email verification
        dto.setVerified(user.isVerified());
        // Profile information
        if (profile != null) {

            dto.setNumber(profile.getNumber());
            dto.setCity(profile.getCity());
            dto.setCountry(profile.getCountry());

            // Profile status
            if (profile.getProfileStatus() != null) {
                dto.setProfileStatus(profile.getProfileStatus().name());
            }

            // Profile image
            if (profile.getProfileImage() != null) {
                dto.setProfileImageBase64(
                        Base64.getEncoder()
                                .encodeToString(profile.getProfileImage())
                );
            }
        }

        return dto;


    }
}
