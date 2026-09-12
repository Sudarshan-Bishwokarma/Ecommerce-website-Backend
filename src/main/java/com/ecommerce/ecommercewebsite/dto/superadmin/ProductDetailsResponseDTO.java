package com.ecommerce.ecommercewebsite.dto.superadmin;

import com.ecommerce.ecommercewebsite.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsResponseDTO {
    // Product information
    private Long productId;
    private String productName;
    private String productDescription;
    private String productImageBase64;
    private ProductStatus status;
    private boolean hasVariants;
    private BigDecimal displayPrice;
    private Integer stock; // total stocks

    // Category & location
    private String productCategory;
    private String districtName;
    // Variants

    // Vendor information
    private VendorDetailsDTO vendor;
    private List<ProductVariantDetailsDTO> variant;

}
