package com.ecommerce.ecommercewebsite.exception;

public class ProductNotFoundException extends ApiException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
