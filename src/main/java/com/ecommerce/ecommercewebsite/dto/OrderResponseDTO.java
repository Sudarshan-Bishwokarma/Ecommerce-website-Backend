package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long orderId;
    private String orderNumber;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private List<CustomerVendorOrderResponseDTO> vendorOrders;
    private PaymentResponseDTO payment;
}

