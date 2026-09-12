package com.ecommerce.ecommercewebsite.dto.vendor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorDashboardResponseDTO {
    private Long totalProducts;

    private Long totalOrders;

    private Long pendingOrders;

    private Long paidOrders;

    private Long processingOrders;

    private Long shippedOrders;

    private Long deliveredOrders;

    private Long cancelledOrders;

    private BigDecimal totalEarnings;
}
