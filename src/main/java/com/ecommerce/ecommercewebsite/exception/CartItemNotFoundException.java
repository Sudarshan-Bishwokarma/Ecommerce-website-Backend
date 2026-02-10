package com.ecommerce.ecommercewebsite.exception;

public class CartItemNotFoundException extends ApiException {

    public CartItemNotFoundException(String message) {
        super(message);
    }
}
