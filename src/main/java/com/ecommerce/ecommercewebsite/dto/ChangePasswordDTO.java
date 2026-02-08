package com.ecommerce.ecommercewebsite.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangePasswordDTO {
    @NotBlank(message = "Old password is required")
    private String oldPassword;
    @NotBlank(message = "New Password is  required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;
    private String confirmPassword;
}
