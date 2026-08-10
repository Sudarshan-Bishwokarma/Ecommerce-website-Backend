package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.PaymentMethod;
import lombok.Data;

@Data
public class PaymentRequestDTO {
    private Long featuredPlanId;
    private PaymentMethod paymentMethod;
}
