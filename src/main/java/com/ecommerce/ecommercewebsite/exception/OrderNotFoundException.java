package com.ecommerce.ecommercewebsite.exception;

public class OrderNotFoundException extends ApiException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}
