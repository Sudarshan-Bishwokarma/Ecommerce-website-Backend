package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.CategoryResponseDTO;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.model.Category;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class CategoryMapper {
    public CategoryResponseDTO mapToDTO(Category category) {
        CategoryResponseDTO responseDTO = new CategoryResponseDTO();
        responseDTO.setCategoryId(category.getId());
        responseDTO.setCategoryName(category.getCategoryName());
        if (category.getCategoryImage() != null) {
            try {
                String base64Image = Base64.getEncoder().encodeToString(category.getCategoryImage());
                responseDTO.setCategoryImage(base64Image);
            } catch (Exception e) {
                throw new ApiException(ProductErrorCode.IMAGE_UPLOADED_FAILED);
            }
        }
        return responseDTO;
    }
}
