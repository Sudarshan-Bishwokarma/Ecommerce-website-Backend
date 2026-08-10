package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.BusinessProfileRequestDTO;
import com.ecommerce.ecommercewebsite.dto.VendorResponseDTO;
import com.ecommerce.ecommercewebsite.enums.VendorAccessStatus;

public interface BusinessProfileService {
    public VendorResponseDTO vendorOnboarding(String email, BusinessProfileRequestDTO businessProfileRequestDTO);

    public VendorAccessStatus getVendorAccessStatus(String email);
}
