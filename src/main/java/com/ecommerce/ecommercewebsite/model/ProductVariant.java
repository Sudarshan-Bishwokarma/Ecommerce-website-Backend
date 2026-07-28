package com.ecommerce.ecommercewebsite.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String size;
    private String color;
    private Integer stock;
    private BigDecimal price;

    @Lob
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
