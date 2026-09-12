package com.ecommerce.ecommercewebsite.services.vendor;

import com.ecommerce.ecommercewebsite.dto.vendor.VendorProfileResponseDTO;
import com.ecommerce.ecommercewebsite.dto.vendor.VendorProfileUpdateRequestDTO;
import com.ecommerce.ecommercewebsite.model.User;

public interface VendorProfileService {
    VendorProfileResponseDTO getVendorProfile(User vendor);

    VendorProfileResponseDTO updateVendorProfile(User vendor, VendorProfileUpdateRequestDTO request);
}
