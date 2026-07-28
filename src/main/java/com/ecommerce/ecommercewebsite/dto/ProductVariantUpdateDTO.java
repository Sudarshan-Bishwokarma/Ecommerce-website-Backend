package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVariantUpdateDTO {
    private Long variantId;

    private String size;

    private String color;

    private BigDecimal price;

    private Integer stock;


}
