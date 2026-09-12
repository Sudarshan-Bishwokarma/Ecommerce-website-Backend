package com.ecommerce.ecommercewebsite.dto.superadmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorDetailsDTO {
    // User information
    private Long vendorId;
    private String name;
    private String email;

    // Profile information
    private String city;
    private String country;
    private String number;
    private String profileImageBase64;

    // Business information
    private String businessName;
    private String businessAddress;
    private String businessDescription;
    private String businessPhone;
    private String businessEmail;
    private String businessWebsite;
}
