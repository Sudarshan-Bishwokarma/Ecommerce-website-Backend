package com.ecommerce.ecommercewebsite.exception;

public class CartNotFoundException extends ApiException {

    public CartNotFoundException(String message) {
        super(message);
    }
}
