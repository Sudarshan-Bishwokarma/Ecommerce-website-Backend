package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryUpdateRequestDTO {
    private String categoryName;
    private MultipartFile categoryImage;
}
