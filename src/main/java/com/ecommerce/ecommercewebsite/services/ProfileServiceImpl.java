package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.ProductRequestDTO;
import com.ecommerce.ecommercewebsite.dto.ProfileRequestDTO;
import com.ecommerce.ecommercewebsite.dto.ProfileResponseDTO;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.enums.ProfileStatus;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.exception.UserNotFoundException;
import com.ecommerce.ecommercewebsite.mappers.ProfileMapper;
import com.ecommerce.ecommercewebsite.model.Profile;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.ProductRepository;
import com.ecommerce.ecommercewebsite.repositories.ProfileRepository;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Base64;


@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileMapper mapper;
    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public ProfileResponseDTO completeProfile(String email, ProfileRequestDTO profileRequestDTO) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        if (profileRepository.findByUser(user).isPresent()) {
            throw new ApiException(AuthErrorCode.PROFILE_ALREADY_EXISTS);
        }
        // validation
        if (profileRequestDTO.getCity() == null || profileRequestDTO.getCity().isBlank() || profileRequestDTO.getCountry() == null || profileRequestDTO.getCountry().isBlank() || profileRequestDTO.getNumber() == null || profileRequestDTO.getNumber().isBlank() || profileRequestDTO.getProfile() == null || profileRequestDTO.getProfile().isEmpty()) {
            throw new ApiException(AuthErrorCode.INVALID_INPUTS);
        }
        Profile profile = new Profile();
        profile.setCity(profileRequestDTO.getCity());
        profile.setCountry(profileRequestDTO.getCountry());
        profile.setNumber(profileRequestDTO.getNumber());
        profile.setUser(user);
        profile.setProfileStatus(ProfileStatus.COMPLETED);
        try {
            profile.setProfileImage(profileRequestDTO.getProfile().getBytes());
        } catch (Exception e) {
            throw new ApiException(AuthErrorCode.PROFILE_UPLOAD_FAILURE);
        }
        profile.setProfileStatus(ProfileStatus.COMPLETED);
        profileRepository.save(profile);
        ProfileResponseDTO responseDTO = mapper.map(profile);
        return responseDTO;
    }
}
