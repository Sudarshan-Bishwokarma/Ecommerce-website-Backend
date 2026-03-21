package com.ecommerce.ecommercewebsite.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    @ManyToOne(fetch = FetchType.LAZY)
//fetch = FetchType.LAZY tells JPA to load the rel  ated entity only when it’s accessed
    @JoinColumn(name = "user_id", nullable = false) //This field is required
    private User user;
    private Double totalAmount;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;  // PENDING, PAID, SHIPPED, DELIVERED, CANCELLED
    private LocalDateTime createdAt;
    private String shippingAddress;
    private String paymentMethod;
    private String notes;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;


}

