package com.ecommerce.ecommercewebsite.dto.users;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserProfileUpdateRequestDTO {
    private String name;
    private String city;
    private String country;
    private String number;
    private MultipartFile profile;
}
