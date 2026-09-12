package com.ecommerce.ecommercewebsite.dto.vendor;

import com.ecommerce.ecommercewebsite.enums.ApprovalStatus;
import com.ecommerce.ecommercewebsite.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessProfileDetailsDTO {
    private Long id;
    private String businessName;
    private String businessAddress;
    private String businessDescription;
    private String categoryName;
    private String businessPhone;
    private String businessEmail;
    private String businessWebsite;
    private ApprovalStatus approvalStatus;
    private boolean profileCompleted;
}
