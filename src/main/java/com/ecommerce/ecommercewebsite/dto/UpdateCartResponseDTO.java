package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateCartResponseDTO {
    private Long cartItemId;

    private Long productId;

    private Long productVariantId;

    private String productName;

    private Integer quantity;
    
    private BigDecimal totalPrice;
}
