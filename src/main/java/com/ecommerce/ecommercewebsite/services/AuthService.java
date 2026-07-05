package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.*;
import com.ecommerce.ecommercewebsite.enums.RoleType;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {
    public JwtResponseDto loginUser(LoginRequestDTO request);

    public String register(RegisterRequestDTO request, RoleType role);

    public String verifyOtp(OtpVerifyDTO request);

    public String resendOtp(ResendOtpDTO request);

    public String changePassword(String email, ChangePasswordDTO change);

    public String sendForgetPasswordOtp(String email);

    public String resetPassword(ResetPasswordDTO request);

}
