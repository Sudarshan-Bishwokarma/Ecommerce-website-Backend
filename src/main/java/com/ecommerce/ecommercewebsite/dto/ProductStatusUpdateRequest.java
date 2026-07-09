package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.ProductStatus;
import lombok.Data;

@Data
public class ProductStatusUpdateRequest {
    private ProductStatus status;
}
