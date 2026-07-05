package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

@Data
public class AddToCartRequestDTO {
    private Long productVariantId;
    private Integer quantity;
}