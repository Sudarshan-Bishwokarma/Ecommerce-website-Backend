package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserFeaturedProductResponseDTO {
    private Long productId;
    private String productName;
    private String productImage;
    private BigDecimal price;
    private String vendorName;
    private String districtName;
}
