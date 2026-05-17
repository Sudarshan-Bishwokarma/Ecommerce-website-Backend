package com.ecommerce.ecommercewebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponseDTO {
    private Long productId;
    private String productName;
    private String productDescription;
    private Double productPrice;
    private String productCategory;
    private String productImageBase64;
    private String districtName;
}   
    