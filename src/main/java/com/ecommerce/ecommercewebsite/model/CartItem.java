package com.ecommerce.ecommercewebsite.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    //  // for  both variant and non-variant products
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    //   // Only used when product has variants
    @ManyToOne
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;
    private int quantity;
    private BigDecimal totalPrice;

}
