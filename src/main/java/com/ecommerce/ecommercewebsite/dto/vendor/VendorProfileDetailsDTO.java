package com.ecommerce.ecommercewebsite.dto.vendor;

import com.ecommerce.ecommercewebsite.enums.ProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorProfileDetailsDTO {
    private Long id;
    private String city;
    private String country;
    private String number;
    private String base64profile;
    private ProfileStatus profileStatus;

}
