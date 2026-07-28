package com.ecommerce.ecommercewebsite.model;

import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String OrderNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false) //This field is required
    private User customer;
    private BigDecimal totalAmount;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;  // PENDING, PAID, SHIPPED, DELIVERED, CANCELLED
    private LocalDateTime createdAt;
    private String shippingAddress;
    private String paymentMethod;
    private String notes;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendorOrder> vendorOrders = new ArrayList<>();


}

