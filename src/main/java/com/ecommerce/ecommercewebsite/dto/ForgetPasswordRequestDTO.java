package com.ecommerce.ecommercewebsite.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgetPasswordRequestDTO {
    @Email
    @NotBlank
    private String email;
}
