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
    @Value("${esewa.vendor-success-url}")
    private String vendorSuccessUrl;

    @Value("${esewa.vendor-failure-url}")
    private String vendorFailureUrl;

    @Value("${esewa.order-success-url}")
    private String orderSuccessUrl;

    @Value("${esewa.order-failure-url}")
    private String orderFailureUrl;
}
// @Value is used to inject values from external configuration files into Java fields.