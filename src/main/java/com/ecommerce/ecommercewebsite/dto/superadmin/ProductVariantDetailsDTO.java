package com.ecommerce.ecommercewebsite.dto.superadmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDetailsDTO {
    private Long id;
    private String size;
    private String color;
    private Integer stock;
    private BigDecimal price;
    private String variantImageBase64;
}
