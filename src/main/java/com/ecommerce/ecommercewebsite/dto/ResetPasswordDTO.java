package com.ecommerce.ecommercewebsite.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordDTO {
    private String otp;
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 8)
    private String newPassword;
    private String confirmNewPassword;
}
