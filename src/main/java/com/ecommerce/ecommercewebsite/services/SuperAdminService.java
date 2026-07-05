package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.VendorResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SuperAdminService {
    public String deleteVendor(Long id);

    public Page<VendorResponseDTO> getAllVendors(int page, int size);

    public String approveVendor(Long id);

    public Page<VendorResponseDTO> getAllPendingVendors(int page, int size);
}
