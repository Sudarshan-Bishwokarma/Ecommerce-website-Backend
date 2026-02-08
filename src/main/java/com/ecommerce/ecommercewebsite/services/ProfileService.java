package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.ProfileResponseDTO;

import java.security.Principal;

public interface ProfileService {
    public ProfileResponseDTO getProfile(String email);
}
