package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.FeaturedRequestStatus;
import lombok.Data;

@Data
public class FeaturedRequestVendorResponseDTO {
    // request information
    private Long requestId;

    private FeaturedRequestStatus status;

    private String adminMessage;

    // product information
    private Long productId;
    private String productName;
    private String productImage;
}

