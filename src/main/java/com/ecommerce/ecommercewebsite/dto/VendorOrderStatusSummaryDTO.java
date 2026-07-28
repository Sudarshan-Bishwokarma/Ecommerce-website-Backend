package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

@Data
public class VendorOrderStatusSummaryDTO {
    private Long pending;
    private Long cancelled;
    private Long delivered;
    private Long paid;
    private Long shipped;
}
