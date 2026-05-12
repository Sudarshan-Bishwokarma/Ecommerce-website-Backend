package com.ecommerce.ecommercewebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRequestDTO {
    private String productName;
    private String productDescription;
    private Double productPrice;
    private Long categoryId;
    private Long districtId;
}
