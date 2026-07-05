package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterRequestDTO {
    private String name;
    private String email;
    private String password;
    private RoleType roleType;
}
