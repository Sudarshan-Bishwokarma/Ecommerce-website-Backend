package com.ecommerce.ecommercewebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String city;
    private String number;
    private String profileImageBase64;
}

/*
Your image is stored as byte[] in the database (BLOB), which is binary data. JSON
cannot directly handle binary, so you need to convert it to a Base64 string before sending it in the API response.
 */