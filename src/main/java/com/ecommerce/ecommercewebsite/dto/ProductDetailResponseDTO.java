package com.ecommerce.ecommercewebsite.dto;

import com.ecommerce.ecommercewebsite.enums.ProductStatus;
import lombok.Data;

import java.util.List;

@Data
public class ProductDetailResponseDTO {
    private Long productId;
    private String productName;
    private String productDescription;
    private String productImageBase64;
    private ProductStatus status;
    private String categoryName;
    private String districtName;

    private Boolean hasVariants;

    private Double productPrice; // base or min price
    private Integer stock;

    private List<ProductVariantDetailResponseDTO> variantsDetails;
}
