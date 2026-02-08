package com.ecommerce.ecommercewebsite.exception;

public class ApiException  extends RuntimeException{
    public ApiException(String message){
            super(message);//   this calls the parent class constructor and  send this message to parent class”
    }
}
