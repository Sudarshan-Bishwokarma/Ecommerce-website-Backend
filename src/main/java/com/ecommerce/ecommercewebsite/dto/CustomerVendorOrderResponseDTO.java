package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CustomerVendorOrderResponseDTO {
    private Long vendorOrderId;

    private Long vendorId;

    private String vendorName;

    private BigDecimal totalAmount;

    private List<OrderItemResponseDTO> items;
}
