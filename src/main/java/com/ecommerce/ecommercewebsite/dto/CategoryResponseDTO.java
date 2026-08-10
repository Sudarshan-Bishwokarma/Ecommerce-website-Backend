package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

@Data
public class CategoryResponseDTO {
    private Long categoryId;
    private String categoryName;
    private String categoryImage;
}
