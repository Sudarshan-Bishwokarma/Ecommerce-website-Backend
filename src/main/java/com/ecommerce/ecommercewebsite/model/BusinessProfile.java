package com.ecommerce.ecommercewebsite.model;

import com.ecommerce.ecommercewebsite.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // business info
    private String businessName;
    private String businessAddress;
    private String businessDescription;
    private Long categoryId;
    // contact  information
    private String businessPhone;
    private String businessEmail;
    private String businessWebsite;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
    private boolean profileCompleted = false;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "document_id")
    private BusinessDocument businessDocument;


}

