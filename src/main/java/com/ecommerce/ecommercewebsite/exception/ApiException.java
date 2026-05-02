package com.ecommerce.ecommercewebsite.exception;

import lombok.Data;


public class ApiException extends RuntimeException {
    private final Enum<?> code;

    public ApiException(Enum<?> code) {
        super(code.name());
        this.code = code;
    }

    public Enum<?> getCode() {
        return code;
    }
}
