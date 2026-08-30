package com.ecommerce.ecommercewebsite.dto.users;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDetailResponseDTO {
    private Long productId;

    private String productName;

    private String productDescription;

    private String productImageBase64;
    private BigDecimal productPrice;
    private boolean hasVariants;
    private Integer stocks; // total stocks

    private String categoryName;

    private String districtName;
    // vendor information
    private VendorSummaryResponseDTO vendor;

    private List<ProductVariantDetailResponseDTO> variants;
}
