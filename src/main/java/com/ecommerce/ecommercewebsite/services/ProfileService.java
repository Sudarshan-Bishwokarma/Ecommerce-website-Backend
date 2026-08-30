package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.ProductRequestDTO;
import com.ecommerce.ecommercewebsite.dto.ProfileRequestDTO;
import com.ecommerce.ecommercewebsite.dto.ProfileResponseDTO;
import com.ecommerce.ecommercewebsite.dto.users.UserProfileUpdateRequestDTO;

import java.security.Principal;

public interface ProfileService {

    public ProfileResponseDTO completeProfile(String email, ProfileRequestDTO profileRequestDTO);

    public ProfileResponseDTO getProfile(String email);

    public ProfileResponseDTO editProfile(String email, UserProfileUpdateRequestDTO updateRequestDTO);
}
