package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

@Data
public class EsewaResponseDTO {
    private String transaction_code;

    private String status;

    private String total_amount;

    private String transaction_uuid;

    private String product_code;

    private String signed_field_names;

    private String signature;
}
