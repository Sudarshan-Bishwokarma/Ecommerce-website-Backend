package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

@Data
public class ProductVariantRequestDTO {

    private String size;
    private String color;
    private Double price;
    private Integer stock;
}
    