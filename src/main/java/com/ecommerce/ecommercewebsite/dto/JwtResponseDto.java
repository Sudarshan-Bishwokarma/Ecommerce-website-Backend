package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.ProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtResponseDto {
    private String email;
    private String token;
    private String role;
    private ProfileStatus status;
}
