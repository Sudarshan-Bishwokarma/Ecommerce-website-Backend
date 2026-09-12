package com.ecommerce.ecommercewebsite.dto.superadmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuperAdminSalesDTO {
    private Long productId;

    private String productName;

    private Long quantitySold;

    private BigDecimal salesValue;
}
