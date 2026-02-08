package com.ecommerce.ecommercewebsite.exception;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException(String message) {
        super(message);
    }

}
