package com.ecommerce.ecommercewebsite.dto.vendor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorDetailsResponseDTO {
    // Vendor information
    private Long id;
    private String name;
    private String email;
    private boolean verified;
    private VendorProfileDetailsDTO profile;
    private BusinessProfileDetailsDTO businessProfile;
    // Business document
    private BusinessDocumentDetailsDTO businessDocument;
}
