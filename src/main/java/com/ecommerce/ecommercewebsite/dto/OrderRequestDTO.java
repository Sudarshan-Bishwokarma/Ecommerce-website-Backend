package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private String fullName;

    private String phoneNumber;

    private Long districtId;

    private String municipality;
    

    private String streetArea;

    private String landmark;

    private PaymentMethod paymentMethod;

    private String notes;
}
