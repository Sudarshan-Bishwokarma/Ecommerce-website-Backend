package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductUpdateRequestDTO {
    private String productName;

    private String productDescription;

    private Long categoryId;

    private Long districtId;

    private Boolean hasVariants;
    //  only for simple products
    private BigDecimal price;

    private Integer stock;
    private MultipartFile productImage;

    private List<ProductVariantUpdateDTO> variants;
    private List<Long> deletedVariantIds;
}
