package com.ecommerce.ecommercewebsite.model;

import com.ecommerce.ecommercewebsite.enums.FeaturedRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class FeaturedRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private User vendor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "featured_plan_id")
    private FeaturedPlan featuredPlan;
    @Enumerated(EnumType.STRING)
    private FeaturedRequestStatus status;
    private String adminMessage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String transactionUuid;
}
