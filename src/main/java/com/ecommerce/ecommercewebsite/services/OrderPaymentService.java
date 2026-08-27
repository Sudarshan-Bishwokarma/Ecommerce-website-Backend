package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.PaymentResponseDTO;
import com.ecommerce.ecommercewebsite.enums.PaymentMethod;

public interface OrderPaymentService {
    PaymentResponseDTO initiatePayment(Long orderId, PaymentMethod paymentMethod);

    String handlePaymentSuccess(String transactionUuid);

    String handlePaymentFailure(String transactionUuid);
}
