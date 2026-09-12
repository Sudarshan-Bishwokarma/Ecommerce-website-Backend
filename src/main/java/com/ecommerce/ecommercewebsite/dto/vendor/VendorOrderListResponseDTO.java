package com.ecommerce.ecommercewebsite.dto.vendor;

import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorOrderListResponseDTO {
    private Long vendorOrderId;
    private Long orderId;
    private String orderNumber;

    // Customer
    private String customerName;

    // Order
    private OrderStatus status;
    private LocalDateTime createdAt;

    // Order Summary
    private Integer totalItems;
    private Integer totalUnits;

    // Vendor Order Amount
    private BigDecimal totalAmount;
}
