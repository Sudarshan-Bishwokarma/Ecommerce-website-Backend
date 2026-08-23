package com.ecommerce.ecommercewebsite.dto.users;

import com.ecommerce.ecommercewebsite.dto.AddToCartResponseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponseDTO {
    private Long cartId;

    private List<AddToCartResponseDTO> items;

    private BigDecimal subtotal;
}
