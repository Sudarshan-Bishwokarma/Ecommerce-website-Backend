package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.ProfileResponseDTO;
import com.ecommerce.ecommercewebsite.model.Profile;
import com.ecommerce.ecommercewebsite.model.User;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class ProfileMapper {
    public ProfileResponseDTO map(Profile profile) {
        User user = profile.getUser();
        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO();
        profileResponseDTO.setId(user.getId());
        profileResponseDTO.setName(user.getName());
        profileResponseDTO.setEmail(user.getEmail());
        profileResponseDTO.setNumber(profile.getNumber());
        profileResponseDTO.setCity(profile.getCity());
        profileResponseDTO.setCountry(profile.getCountry());
        if (profile.getProfileImage() != null) {
            String base64 = Base64.getEncoder().encodeToString(profile.getProfileImage());
            profileResponseDTO.setProfileImageBase64(base64);
        }
        profileResponseDTO.setProfileStatus(profile.getProfileStatus());
        return profileResponseDTO;
    }
}