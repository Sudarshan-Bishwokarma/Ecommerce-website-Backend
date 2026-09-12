package com.ecommerce.ecommercewebsite.services.superadmin;

import com.ecommerce.ecommercewebsite.dto.CategoryRequestDTO;
import com.ecommerce.ecommercewebsite.dto.CategoryResponseDTO;
import com.ecommerce.ecommercewebsite.dto.CategoryUpdateRequestDTO;
import com.ecommerce.ecommercewebsite.enums.PaymentStatus;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.mappers.CategoryMapper;
import com.ecommerce.ecommercewebsite.model.Category;
import com.ecommerce.ecommercewebsite.repositories.CategoryRepository;
import com.ecommerce.ecommercewebsite.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class SuperAdminCategoryServiceImpl implements SuperAdminCategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDTO addCategory(CategoryRequestDTO categoryRequestDTO) {
        boolean value = categoryRepository.existsByCategoryNameIgnoreCase(categoryRequestDTO.getCategoryName());
        if (value) {
            throw new ApiException(ProductErrorCode.CATEGORY_ALREADY_EXISTS);
        }
        Category category = new Category();
        category.setCategoryName(categoryRequestDTO.getCategoryName());
        if (categoryRequestDTO.getCategoryImage() != null) {
            try {
                category.setCategoryImage(categoryRequestDTO.getCategoryImage().getBytes());
            } catch (IOException e) {
                throw new ApiException(ProductErrorCode.IMAGE_UPLOADED_FAILED);
            }
        }
        Category savedCategory = categoryRepository.save(category);
        CategoryResponseDTO categoryResponseDTO = categoryMapper.mapToDTO(savedCategory);
        return categoryResponseDTO;
    }

    @Override
    public CategoryResponseDTO updateCategory(CategoryUpdateRequestDTO categoryUpdateRequestDTO, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ApiException(ProductErrorCode.CATEGORY_NOT_FOUND)
                );

        if (!category.getCategoryName()
                .equalsIgnoreCase(categoryUpdateRequestDTO.getCategoryName())
                &&
                categoryRepository.existsByCategoryNameIgnoreCase(
                        categoryUpdateRequestDTO.getCategoryName()
                )) {

            throw new ApiException(
                    ProductErrorCode.CATEGORY_ALREADY_EXISTS
            );
        }

        category.setCategoryName(categoryUpdateRequestDTO.getCategoryName());
        if (categoryUpdateRequestDTO.getCategoryImage() != null) {
            try {
                category.setCategoryImage(categoryUpdateRequestDTO.getCategoryImage().getBytes());
            } catch (IOException e) {
                throw new ApiException(ProductErrorCode.IMAGE_UPLOADED_FAILED);
            }
        }
        CategoryResponseDTO categoryResponseDTO = categoryMapper.mapToDTO(categoryRepository.save(category));
        return categoryResponseDTO;
    }

    @Override
    public CategoryResponseDTO getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new ApiException(ProductErrorCode.CATEGORY_NOT_FOUND)
                );
    
        return categoryMapper.mapToDTO(category);

    }
}
