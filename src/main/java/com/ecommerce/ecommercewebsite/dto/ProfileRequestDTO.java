package com.ecommerce.ecommercewebsite.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileRequestDTO {
    private String city;
    private String country;
    private String number;
    private MultipartFile profile;

}
