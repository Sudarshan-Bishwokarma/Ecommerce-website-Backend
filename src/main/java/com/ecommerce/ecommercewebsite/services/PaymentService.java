package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.PaymentRequestDTO;
import com.ecommerce.ecommercewebsite.dto.PaymentResponseDTO;

public interface PaymentService {
    PaymentResponseDTO initiatePayment(Long featuredRequestId, PaymentRequestDTO paymentRequestDTO);

    String handlePaymentSuccess(String transactionUuid);

    String handlePaymentFailure(String transactionUuid);
}
