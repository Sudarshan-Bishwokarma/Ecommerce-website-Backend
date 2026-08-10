package com.ecommerce.ecommercewebsite.services.payment;

import com.ecommerce.ecommercewebsite.dto.PaymentRequestDTO;
import com.ecommerce.ecommercewebsite.dto.PaymentResponseDTO;
import com.ecommerce.ecommercewebsite.model.FeaturedPlan;

import java.math.BigDecimal;

public interface EsewaPaymentService {
    PaymentResponseDTO createPayment(Long featuredRequestId, FeaturedPlan featuredPlan);

    String verifyPayment(String data);


}
