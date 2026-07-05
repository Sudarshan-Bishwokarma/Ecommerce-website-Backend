package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;

@Data
public class BusinessDocumentResponseDTO {
    private Long id;
    private String documentType;
    private String base64document;
}
