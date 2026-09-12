package com.ecommerce.ecommercewebsite.dto.superadmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuperAdminDistrictCommissionDTO {
    private Long districtId;
    private String districtName;
    private Long orderCount;
    private BigDecimal salesAmount;
    private BigDecimal commissionAmount;
}
