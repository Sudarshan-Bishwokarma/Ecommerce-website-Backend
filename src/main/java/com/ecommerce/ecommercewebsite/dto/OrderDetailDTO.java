package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.model.OrderStatus;
import lombok.Data;

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
    Double totalPrice;
    List<OrderItemDTO> orderItems;

}
