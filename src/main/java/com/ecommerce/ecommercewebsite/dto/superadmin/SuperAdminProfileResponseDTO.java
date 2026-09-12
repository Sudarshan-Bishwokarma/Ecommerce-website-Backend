package com.ecommerce.ecommercewebsite.dto.superadmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuperAdminProfileResponseDTO {
    private String name;
    private String email;
    private String number;
    private String city;
    private String country;
    private String role;
    private boolean verified;
    private String profileStatus;
    private String profileImageBase64;
}
