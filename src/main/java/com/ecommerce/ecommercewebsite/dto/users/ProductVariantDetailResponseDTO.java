package com.ecommerce.ecommercewebsite.dto.users;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVariantDetailResponseDTO {
    private Long id;
    private String size;
    private String color;
    private BigDecimal price;
    private Integer stock;
    private String sku;
    private String variantImageBase64;
}
