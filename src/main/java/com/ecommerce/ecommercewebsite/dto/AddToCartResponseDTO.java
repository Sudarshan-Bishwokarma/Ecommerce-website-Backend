package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

@Data
public class AddToCartResponseDTO {
    private Long cartItemId;
    private Long productId;
    private String productName;
    private Double productPrice;
    private Integer quantity;
    private Double totalPrice;
}
