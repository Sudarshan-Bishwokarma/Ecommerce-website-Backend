package com.ecommerce.ecommercewebsite.services.vendor;

import com.ecommerce.ecommercewebsite.dto.vendor.VendorDashboardResponseDTO;
import com.ecommerce.ecommercewebsite.model.User;

public interface VendorDashboardService {
    VendorDashboardResponseDTO getVendorDashboard(User vendor);
}
