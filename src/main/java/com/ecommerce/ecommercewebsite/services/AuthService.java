package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.*;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    public JwtResponseDto loginUser(LoginRequestDTO request);

    public String registerUser(RegisterRequestDTO request, MultipartFile profile);

    public String verifyOtp(OtpVerifyDTO request);

    public String changePassword(String email, ChangePasswordDTO change);

    public String sendForgetPasswordOtp(String email);

    public String resetPassword(ResetPasswordDTO request);

}
