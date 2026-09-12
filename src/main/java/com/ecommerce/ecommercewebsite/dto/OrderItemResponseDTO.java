package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponseDTO {

    private Long productId;
    private String productName;

    private String size;
    private String color;

    private Integer quantity;

    private BigDecimal priceAtPurchase;
    private BigDecimal totalPrice;
}
