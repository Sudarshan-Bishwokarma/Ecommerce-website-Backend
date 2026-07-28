package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVariantDetailResponseDTO {
    private Long id;
    private String size;
    private String color;
    private BigDecimal price;
    private Integer stock;
    private String variantImageBase64;
}
