package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.BusinessProfileRequestDTO;
import com.ecommerce.ecommercewebsite.dto.VendorResponseDTO;

public interface BusinessProfileService {
    public VendorResponseDTO vendorOnboarding(String email, BusinessProfileRequestDTO businessProfileRequestDTO);

    public boolean isBusinessProfileCompleted(String email);
}
