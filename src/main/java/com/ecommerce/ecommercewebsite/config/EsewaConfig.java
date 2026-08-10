package com.ecommerce.ecommercewebsite.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class EsewaConfig {
    @Value("${esewa.product-code}")
    private String productCode;

    @Value("${esewa.secret-key}")
    private String secretKey;

    @Value("${esewa.payment-url}")
    private String paymentUrl;
    @Value("${esewa.success-url}")
    private String successUrl;
    @Value("${esewa.failure-url}")
    private String failureUrl;
}
// @Value is used to inject values from external configuration files into Java fields.