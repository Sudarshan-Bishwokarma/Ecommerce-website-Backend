package com.ecommerce.ecommercewebsite.services.superadmin;

import com.ecommerce.ecommercewebsite.dto.superadmin.SuperAdminProfileResponseDTO;

public interface SuperAdminProfileService {
    SuperAdminProfileResponseDTO getProfile(String email);
}
