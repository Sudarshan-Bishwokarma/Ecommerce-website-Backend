package com.ecommerce.ecommercewebsite.dto.vendor;

import com.ecommerce.ecommercewebsite.enums.ApprovalStatus;
import com.ecommerce.ecommercewebsite.enums.ProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorProfileResponseDTO {
    // User information
    private Long vendorId;
    private String name;
    private String email;

    // Personal profile information
    private String city;
    private String country;
    private String number;
    private String profileImageBase64;
    private ProfileStatus profileStatus;

    // Business information
    private Long businessProfileId;
    private String businessName;
    private String businessAddress;
    private String businessDescription;
    private Long categoryId;
    private String businessPhone;
    private String businessEmail;
    private String businessWebsite;

    // Business approval
    private ApprovalStatus approvalStatus;
    private boolean profileCompleted;

    // Business document information
    private String documentType;
    private String documentFileName;
    private String documentContentType;
}
