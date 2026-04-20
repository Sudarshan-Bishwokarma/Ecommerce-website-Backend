package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

@Data
public class OrderStatusSummaryDTO {
    private long pending;
    private long cancelled;
    private long delivered;
    private long paid;
    private long shipped;
}
