package com.ecommerce.ecommercewebsite.dto.superadmin;

import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuperAdminOrderResponseDTO {
    private Long orderId;

    private String orderNumber;

    private String customerName;

    private String customerEmail;

    private List<String> vendorNames;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private LocalDateTime createdAt;
}
