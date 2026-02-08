package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.ProfileResponseDTO;
import com.ecommerce.ecommercewebsite.exception.UserNotFoundException;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Base64;


@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public ProfileResponseDTO getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO();
        profileResponseDTO.setId(user.getId());
        profileResponseDTO.setEmail(user.getEmail());
        profileResponseDTO.setName(user.getName());
        profileResponseDTO.setCity(user.getCity());
        profileResponseDTO.setNumber(user.getNumber());
        if (user.getProfile() != null) {
            String base64 = Base64.getEncoder().encodeToString(user.getProfile());
            profileResponseDTO.setProfileImageBase64(base64);
        }
        return profileResponseDTO;
    }
}
