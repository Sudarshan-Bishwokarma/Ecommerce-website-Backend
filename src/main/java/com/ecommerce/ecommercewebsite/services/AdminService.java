package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.VendorResponseDTO;
import com.ecommerce.ecommercewebsite.dto.AdminUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {
    public VendorResponseDTO editAdmin(AdminUpdateDTO adminUpdateDTO, MultipartFile file, Long id);
}
