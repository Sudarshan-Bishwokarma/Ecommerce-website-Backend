package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

@Data
public class AddToCartRequestDTO {
    private Long productId;
    private Integer quantity;
}
//The frontend sends the data, and Spring automatically puts it into the DTO object.