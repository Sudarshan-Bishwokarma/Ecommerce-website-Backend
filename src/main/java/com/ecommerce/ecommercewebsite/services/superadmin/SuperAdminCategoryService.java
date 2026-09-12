package com.ecommerce.ecommercewebsite.services.superadmin;

import com.ecommerce.ecommercewebsite.dto.CategoryRequestDTO;
import com.ecommerce.ecommercewebsite.dto.CategoryResponseDTO;
import com.ecommerce.ecommercewebsite.dto.CategoryUpdateRequestDTO;

public interface SuperAdminCategoryService {
    public CategoryResponseDTO addCategory(CategoryRequestDTO categoryRequestDTO);

    public CategoryResponseDTO updateCategory(CategoryUpdateRequestDTO categoryUpdateRequestDTO, Long categoryId);

    public CategoryResponseDTO getCategory(Long categoryId);

}
