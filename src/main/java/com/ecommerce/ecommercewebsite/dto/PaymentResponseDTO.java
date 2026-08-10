package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentResponseDTO {
    private String paymentUrl;

    private String transactionId;

    private BigDecimal amount;
    private EsewaPaymentRequestDTO paymentData;
}
