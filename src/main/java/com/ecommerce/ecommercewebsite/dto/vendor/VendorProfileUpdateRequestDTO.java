package com.ecommerce.ecommercewebsite.dto.vendor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorProfileUpdateRequestDTO {
    private String name;
    private String number;
    private String city;
    private String country;

    private MultipartFile profileImage;
}
