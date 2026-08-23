package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddToCartResponseDTO {
    private Long cartItemId;

    private Long productVariantId;

    private Long productId;

    private String productName;
    private String productImage;

    private String size;

    private String color;

    private BigDecimal productPrice;

    private Integer quantity;
    private Integer availableStock;

    private BigDecimal totalPrice;  // total  of each cart item
}
