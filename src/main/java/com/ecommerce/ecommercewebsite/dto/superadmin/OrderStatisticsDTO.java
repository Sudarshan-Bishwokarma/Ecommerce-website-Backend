package com.ecommerce.ecommercewebsite.dto.superadmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatisticsDTO {
    private Long pendingPayment;
    private Long paid;
    private Long processing;
    private Long shipped;
    private Long delivered;
    private Long cancelled;
    private Long paymentFailed;
}
