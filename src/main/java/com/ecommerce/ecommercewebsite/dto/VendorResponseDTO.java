package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.ApprovalStatus;
import com.ecommerce.ecommercewebsite.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VendorResponseDTO {
    // vendor info
    private Long id;
    private String name;
    private String email;
    private String city;
    private String number;
    private String base64profile;
    private boolean verified;
    // vendor business info
    private String businessName;
    private Long categoryId;
    private String businessAddress;
    private String businessDescription;
    private String businessEmail;
    private String businessWebsite;
    private String businessPhone;
    private ApprovalStatus approvalStatus;
    private boolean businessProfileCompleted;


}
