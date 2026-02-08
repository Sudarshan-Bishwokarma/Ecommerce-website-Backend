package com.ecommerce.ecommercewebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminRequestDTO {
    private String name;
    private String email;
    private String password;
    private String city;
    private String number;
    private String profile;
}   
