package com.ecommerce.ecommercewebsite.model;

import com.ecommerce.ecommercewebsite.enums.PaymentMethod;
import com.ecommerce.ecommercewebsite.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FeaturedPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "featured_request_id")
    private FeaturedRequest featuredRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "featured_plan_id")
    private FeaturedPlan featuredPlan;

    private BigDecimal amount;
    private String transactionId;

    private String paymentReference;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
}
