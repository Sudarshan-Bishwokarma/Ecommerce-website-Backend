package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private String productName;
    private Integer quantity;
    private Double subTotal;
}
