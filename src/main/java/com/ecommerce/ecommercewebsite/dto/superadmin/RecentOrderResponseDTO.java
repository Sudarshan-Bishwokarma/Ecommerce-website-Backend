package com.ecommerce.ecommercewebsite.dto.superadmin;

import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentOrderResponseDTO {
    private Long orderId;

    private String orderNumber;

    private String customerName;

    private List<String> vendorNames;

    private BigDecimal totalAmount;

    private OrderStatus status;
}
