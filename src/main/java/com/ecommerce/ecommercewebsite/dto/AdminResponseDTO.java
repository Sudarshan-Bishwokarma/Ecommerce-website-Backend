package com.ecommerce.ecommercewebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String city;
    private String number;

}
