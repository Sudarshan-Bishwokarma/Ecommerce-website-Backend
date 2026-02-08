package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.AdminRequestDTO;
import com.ecommerce.ecommercewebsite.dto.AdminResponseDTO;
import com.ecommerce.ecommercewebsite.dto.AdminUpdateDTO;
import jakarta.mail.Multipart;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {
    public AdminResponseDTO editAdmin(AdminUpdateDTO adminUpdateDTO, MultipartFile file, Long id);
}
