package com.ecommerce.ecommercewebsite.dto.superadmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDetailDTO {
    private Long orderItemId;

    private Long productId;
    private String productName;

    private Long variantId;
    private String size;
    private String color;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
    private BigDecimal totalPrice;
}
