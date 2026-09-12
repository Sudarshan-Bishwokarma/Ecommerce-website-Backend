package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import com.ecommerce.ecommercewebsite.enums.PaymentMethod;
import com.ecommerce.ecommercewebsite.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VendorOrderResponseDTO {
    private Long vendorOrderId;
    private Long orderId;
    private String orderNumber;

    // Customer Information
    private String customerName;
    private String customerEmail;
    private String phoneNumber;

    // Delivery Information
    private String district;
    private String municipality;
    private String streetArea;
    private String landmark;

    // Order Information
    private OrderStatus status;
    private LocalDateTime createdAt;

    // Payment Information
    private BigDecimal paymentAmount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionUuid;
    private LocalDateTime paidAt;

    // Vendor Financial Information
    private BigDecimal totalAmount;
    private BigDecimal commissionAmount;
    private BigDecimal vendorEarning;

    // Products
    private List<OrderItemResponseDTO> items;
}
