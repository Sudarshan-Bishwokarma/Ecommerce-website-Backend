package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductRequestUpdateDTO {
    private String productName;
    private String productDescription;
    private Long categoryId;
    private Long districtId;
    private MultipartFile productImage;
    private List<ProductVariantUpdateRequestDTO> variants;
}
