package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VendorOrderResponseDTO {
    private Long vendorOrderId;

    private Long orderId;

    // Customer information
    private String customerName;
    private String customerEmail;
    private String shippingAddress;

    // order details
    private OrderStatus status;
    private LocalDateTime createdAt;

    // money details
    private BigDecimal totalAmount;
    private BigDecimal commissionAmount;
    private BigDecimal vendorEarning;

    // Products in this vendor order
    private List<OrderItemResponseDTO> items;
}
