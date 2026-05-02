package com.ecommerce.ecommercewebsite.exception;

import com.ecommerce.ecommercewebsite.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //  Handle custom exceptions (your ApiException + subclasses)
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Object>> handle(ApiException ex) {

        return ResponseEntity.badRequest().body(
                new ApiResponse<>(
                        ex.getCode().name(),
                        Map.of("code", ex.getCode().name())
                )
        );
    }

    //  Handle Spring Security login error (wrong password, etc.)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        ApiResponse<Object> response =
                new ApiResponse<>("Invalid email or password", null);

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Handle validation errors (@Valid DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        ApiResponse<Object> response =
                new ApiResponse<>("Validation failed", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Fallback handler (VERY IMPORTANT - catches everything else)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex) {
        ApiResponse<Object> response =
                new ApiResponse<>(ex.getMessage(), null);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}