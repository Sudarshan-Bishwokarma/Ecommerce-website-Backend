package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.ProductResponseDTO;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.model.ProductVariant;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

@Component
public class ProductMapper {

    public ProductResponseDTO mapToDTO(Product product) {

        ProductResponseDTO dto = new ProductResponseDTO();

        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductDescription(product.getProductDescription());
        dto.setStatus(product.getStatus());
        dto.setFeatured(product.isFeatured());
        if (product.getCategory() != null) {
            dto.setProductCategory(product.getCategory().getCategoryName());
        }

        if (product.getDistrict() != null) {
            dto.setDistrictName(product.getDistrict().getDistrictName());
        }

        // image
        if (product.getProductImage() != null) {
            String base64 = Base64.getEncoder()
                    .encodeToString(product.getProductImage());
            dto.setProductImageBase64(base64);
        }

        // variants
        List<ProductVariant> variants = product.getProductVariants();

        if (variants != null && !variants.isEmpty()) {

            dto.setHasVariants(true);
            int totalStock = 0;

            for (ProductVariant v : variants) {

                if (v.getStock() != null) {
                    totalStock += v.getStock();
                }
            }
            dto.setStock(totalStock);

        } else {

            dto.setHasVariants(false);
            dto.setProductPrice(product.getPrice());
            dto.setStock(product.getStock());
        }
        dto.setProductPrice(product.getDisplayPrice());
        return dto;
    }
}