package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateVendorOrderStatusResponseDTO {
    private Long vendorOrderId;

    private OrderStatus status;

    private LocalDateTime updatedAt;

}
