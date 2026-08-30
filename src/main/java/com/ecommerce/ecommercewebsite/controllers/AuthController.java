package com.ecommerce.ecommercewebsite.controllers;

import com.ecommerce.ecommercewebsite.dto.*;
import com.ecommerce.ecommercewebsite.enums.RoleType;
import com.ecommerce.ecommercewebsite.response.ApiResponse;
import com.ecommerce.ecommercewebsite.services.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponseDto>> login(@RequestBody LoginRequestDTO request) {
        JwtResponseDto response = authService.loginUser(request);
        ApiResponse<JwtResponseDto> apiResponse = new ApiResponse<>("Login Successful!", response);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody RegisterRequestDTO request) {
        RoleType role = request.getRoleType();
        System.out.println("Username:" + request.getName());
        String result = authService.register(request, role);
        ApiResponse<String> response = new ApiResponse<>("Success", result);
        return ResponseEntity.ok(response);

    }


    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<String>> verifyOtp(@RequestBody OtpVerifyDTO request) {
        String response = authService.verifyOtp(request);
        ApiResponse<String> apiResponse = new ApiResponse<>("Success", response);
        return ResponseEntity.ok(apiResponse);
    }

    // resend otp
    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponse<String>> resendOtp(@RequestBody ResendOtpDTO request) {
        String response = authService.resendOtp(request);
        ApiResponse<String> apiResponse = new ApiResponse<>("Success", response);
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@Valid @RequestBody ChangePasswordDTO request, Principal principal) {
        String email = principal.getName();
        String result = authService.changePassword(email, request);
        ApiResponse<String> response = new ApiResponse<>("Password Changed Successfully", result);
        return ResponseEntity.ok(response);
    }

    // send otp
    @PostMapping("/forget-password")
    public ResponseEntity<String> forgetPassword(@Valid @RequestBody ForgetPasswordRequestDTO requestDTO) {
        String email = requestDTO.getEmail();
        String result = authService.sendForgetPasswordOtp(email);
        return ResponseEntity.ok(result);
    }


    // reset   password
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordDTO request) {
        String result = authService.resetPassword(request);
        ApiResponse<String> response = new ApiResponse<>(result, null);
        return ResponseEntity.ok(response);
    }

}