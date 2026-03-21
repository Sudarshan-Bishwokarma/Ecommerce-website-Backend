package com.ecommerce.ecommercewebsite.controllers;

import com.ecommerce.ecommercewebsite.dto.*;
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
    AuthService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponseDto>> login(@RequestBody LoginRequestDTO request) {
        JwtResponseDto response = userService.loginUser(request);
        ApiResponse<JwtResponseDto> apiResponse = new ApiResponse<>("Login Successful!", response);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<String>> register(@RequestPart("data") String data, @RequestPart("file") MultipartFile profile) throws IOException {
        // Convert JSON string to DTO
        RegisterRequestDTO request = new ObjectMapper().readValue(data, RegisterRequestDTO.class);
        System.out.println("Username:" + request.getName());
        System.out.println("File Name:" + profile.getOriginalFilename());
        String result = userService.registerUser(request, profile);
        ApiResponse<String> response = new ApiResponse<>(result, "null");
        return ResponseEntity.ok(response);

    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerifyDTO request) {
        String response = userService.verifyOtp(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(@Valid @RequestBody ChangePasswordDTO request, Principal principal) {
        String email = principal.getName();
        String result = userService.changePassword(email, request);
        ApiResponse<String> response = new ApiResponse<>("Password Changed Successfully", result);
        return ResponseEntity.ok(response);
    }

    // send otp
    @PostMapping("/forget-password")
    public ResponseEntity<String> forgetPassword(@Valid @RequestBody ForgetPasswordRequestDTO requestDTO) {
        String email = requestDTO.getEmail();
        String result = userService.sendForgetPasswordOtp(email);
        return ResponseEntity.ok(result);
    }

    // reset   password
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordDTO request) {
        String result = userService.resetPassword(request);
        ApiResponse<String> response = new ApiResponse<>(result, null);
        return ResponseEntity.ok(response);
    }

}