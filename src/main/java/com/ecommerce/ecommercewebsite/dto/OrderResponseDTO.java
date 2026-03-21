package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

@Data
public class OrderResponseDTO {
    private Long orderId;
    private String orderStatus;
    private Double totalPrice;
}
    
