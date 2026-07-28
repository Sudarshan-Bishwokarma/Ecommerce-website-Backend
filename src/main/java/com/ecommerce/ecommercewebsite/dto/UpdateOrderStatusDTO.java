package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusDTO {
    private OrderStatus orderStatus;
}
