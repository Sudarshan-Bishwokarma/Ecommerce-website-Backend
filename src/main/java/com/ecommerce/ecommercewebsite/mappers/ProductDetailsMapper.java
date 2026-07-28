package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.ProductDetailResponseDTO;
import com.ecommerce.ecommercewebsite.dto.ProductVariantDetailResponseDTO;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.model.ProductVariant;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class ProductDetailsMapper {
    public ProductDetailResponseDTO mapToDTO(Product product) {
        ProductDetailResponseDTO dto = new ProductDetailResponseDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductDescription(product.getProductDescription());
        dto.setStatus(product.getStatus());
        if (product.getProductImage() != null) {
            String base64 = Base64.getEncoder().encodeToString(product.getProductImage());
            dto.setProductImageBase64(base64);
        }
        dto.setCategoryId(product.getCategory().getId());
        dto.setCategoryName(product.getCategory().getCategoryName());
        dto.setDistrictId(product.getDistrict().getId());
        dto.setDistrictName(product.getDistrict().getDistrictName());
        dto.setHasVariants(product.isHasVariants());
        List<ProductVariant> variants = product.getProductVariants();
        List<ProductVariantDetailResponseDTO> variantsList = new ArrayList<>();
        if (variants != null && !variants.isEmpty()) {
            BigDecimal min_Price = null;
            int totalStock = 0;
            for (ProductVariant v : variants) {
                if (v.getPrice() != null && (min_Price == null || v.getPrice().compareTo(min_Price) < 0)) {
                    min_Price = v.getPrice();
                }
                totalStock += v.getStock();
            }
            dto.setProductPrice(min_Price);
            dto.setStock(totalStock);

            for (ProductVariant v : variants) {
                ProductVariantDetailResponseDTO detail = new ProductVariantDetailResponseDTO();
                detail.setId(v.getId());
                detail.setPrice(v.getPrice());
                detail.setStock(v.getStock());
                detail.setSize(v.getSize());
                detail.setColor(v.getColor());
                if (v.getImage() != null) {
                    String base64 = Base64.getEncoder().encodeToString(v.getImage());
                    detail.setVariantImageBase64(base64);
                }
                variantsList.add(detail);
            }
            dto.setVariantsDetails(variantsList);

        } else {
            dto.setProductPrice(product.getPrice());
            dto.setStock(product.getStock());
        }
        return dto;
    }
}

