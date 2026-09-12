package com.ecommerce.ecommercewebsite.dto.vendor;

import com.ecommerce.ecommercewebsite.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDocumentDetailsDTO {
    private Long id;
    private DocumentType documentType;
    private String fileName;
    private LocalDateTime uploadedAt;
    private String base64Document;
    private String contentType;
}
