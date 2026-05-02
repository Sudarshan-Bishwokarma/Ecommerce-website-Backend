package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateOrderStatusResponseDTO {
    Long orderId;
    OrderStatus orderStatus;
    String message;
    LocalDateTime updateTime;

}
