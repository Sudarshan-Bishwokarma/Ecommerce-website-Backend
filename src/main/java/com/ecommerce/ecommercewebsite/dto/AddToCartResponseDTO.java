package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

@Data
public class AddToCartResponseDTO {
    private Long cartItemId;

    private Long productVariantId;

    private Long productId;

    private String productName;

    private String size;

    private String color;

    private Double productPrice;

    private Integer quantity;

    private Double totalPrice;
}
