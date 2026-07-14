package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

@Data
public class ProductVariantUpdateDTO {
    private Long variantId;

    private String size;

    private String color;

    private Double price;

    private Integer stock;


}
