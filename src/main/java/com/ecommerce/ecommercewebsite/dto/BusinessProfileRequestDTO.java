package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.DocumentType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BusinessProfileRequestDTO {
    // business info
    private String businessName;
    private String businessAddress;
    private String businessDescription;
    private Long categoryId;

    // contact information
    private String businessPhone;
    private String businessEmail;
    private String businessWebsite;
    // document details
    private DocumentType documentType;
    private MultipartFile document;

}
