package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailDTO {
    private Long orderId;
    String userEmail;
    String shippingAddress;
    LocalDateTime orderDateTime;
    String paymentMethod;
    OrderStatus orderStatus;
    BigDecimal totalPrice;
    List<OrderItemDTO> orderItems;

}
