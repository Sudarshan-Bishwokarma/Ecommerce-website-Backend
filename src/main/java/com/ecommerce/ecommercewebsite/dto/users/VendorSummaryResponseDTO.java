package com.ecommerce.ecommercewebsite.dto.users;

import lombok.Data;

@Data
public class VendorSummaryResponseDTO {
    private Long vendorId;

    private String businessName;

    private String businessAddress;

    private String businessDescription;

    private String businessPhone;

    private String businessEmail;

    private String businessWebsite;


}
