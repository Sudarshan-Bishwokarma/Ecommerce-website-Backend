package com.ecommerce.ecommercewebsite.exception;

import com.ecommerce.ecommercewebsite.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
//Automatically converts handler return to JSON  and   combination of @ResponseBody and @ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class) //tells Spring “catch all exceptions of this type or subclasses
    public ResponseEntity<ApiResponse<Object>> handleApiException(ApiException ex) {
        ApiResponse<Object> response = new ApiResponse<>(ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); //  400 bad request

    }

    // handle  validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        ApiResponse<Object> response =
                new ApiResponse<>("Validation failed", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}

/*
The GlobalExceptionHandler class is automatically called by Spring whenever an exception
 is thrown in your application and not handled inside the controller or service. You do not call it manually.
 */