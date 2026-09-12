package com.ecommerce.ecommercewebsite.dto.superadmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuperAdminCommissionDTO {
    private Long vendorId;
    private String vendorName;
    private Long orderCount;
    private BigDecimal salesAmount;
    private BigDecimal commissionAmount;
}
