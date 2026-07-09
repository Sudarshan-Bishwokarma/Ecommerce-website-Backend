package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

@Data
public class ProductVariantDetailResponseDTO {
    private Long id;
    private String size;
    private String color;
    private Double price;
    private Integer stock;
    private String variantImageBase64;
}
    