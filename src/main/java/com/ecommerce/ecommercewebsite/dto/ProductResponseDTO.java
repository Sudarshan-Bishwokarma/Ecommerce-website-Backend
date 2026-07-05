package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponseDTO {
    private Long productId;
    private String productName;
    private Integer stock;
    private String productDescription;
    private ProductStatus status;
    // min price for variants and normal price for    no variants
    private Double productPrice;
    private String productCategory;
    private String productImageBase64;
    private String districtName;
    private Boolean hasVariants;
}   
    