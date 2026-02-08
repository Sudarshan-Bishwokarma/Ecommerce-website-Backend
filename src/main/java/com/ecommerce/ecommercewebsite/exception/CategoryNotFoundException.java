package com.ecommerce.ecommercewebsite.exception;

public class CategoryNotFoundException  extends  ApiException{
    public CategoryNotFoundException(String message){
        super(message);
    }
}
