package com.ecommerce.ecommercewebsite.services.payment;

import com.ecommerce.ecommercewebsite.dto.PaymentRequestDTO;
import com.ecommerce.ecommercewebsite.dto.PaymentResponseDTO;
import com.ecommerce.ecommercewebsite.model.FeaturedPlan;
import com.ecommerce.ecommercewebsite.model.Order;

public interface EsewaPaymentService {
    // vendor - featured product
    PaymentResponseDTO createPayment(Long featuredRequestId, FeaturedPlan featuredPlan);

    String verifyPayment(String data);

    // user
    PaymentResponseDTO createOrderPayment(Order order);


}
