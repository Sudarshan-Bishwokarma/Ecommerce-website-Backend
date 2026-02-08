package com.ecommerce.ecommercewebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OtpVerifyDTO {
    private String otp;
    private String email;

}
