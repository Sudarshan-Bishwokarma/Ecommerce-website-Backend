package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.AdminRequestDTO;
import com.ecommerce.ecommercewebsite.dto.AdminResponseDTO;
import com.ecommerce.ecommercewebsite.model.User;

import java.util.List;

public interface SuperAdminService {
 public AdminResponseDTO addAdmin(AdminRequestDTO request);
 public AdminResponseDTO updateAdmin(AdminRequestDTO request , Long id);
 public String deleteAdmin(Long id);
 public List<AdminResponseDTO> getAllAdmins();
}
