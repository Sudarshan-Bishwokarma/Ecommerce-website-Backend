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
public class VendorOrderDetailDTO {
    private Long vendorOrderId;
    private Long vendorId;
    private String vendorName;

    private OrderStatus status;
    private LocalDateTime createdAt;

    private BigDecimal totalAmount;
    private BigDecimal commissionAmount;
    private BigDecimal vendorEarning;

    private List<OrderItemDetailDTO> items;
}
