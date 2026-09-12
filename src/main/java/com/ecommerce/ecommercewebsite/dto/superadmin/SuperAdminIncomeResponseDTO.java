package com.ecommerce.ecommercewebsite.dto.superadmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuperAdminIncomeResponseDTO {
    private BigDecimal orderCommission;
    private BigDecimal featuredIncome;
    private BigDecimal totalIncome;
}
