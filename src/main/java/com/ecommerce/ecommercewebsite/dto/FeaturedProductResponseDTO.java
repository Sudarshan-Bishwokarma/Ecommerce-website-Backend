package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.FeaturedRequestStatus;
import lombok.Data;

@Data
public class FeaturedProductResponseDTO {
    private Long requestId;
    //  product information
    private Long productId;
    private String productName;
    private String productImage;
    private String productDescription;
    // vendor information
    private Long vendorId;
    private String vendorName;
    // product classification
    private String categoryName;
    private String districtName;

    // request information
    private FeaturedRequestStatus status;
    private String adminMessage;
}
