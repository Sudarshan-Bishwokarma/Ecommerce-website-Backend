package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.ProductDetailResponseDTO;
import com.ecommerce.ecommercewebsite.dto.ProductVariantDetailResponseDTO;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.model.ProductVariants;
import org.springframework.stereotype.Component;

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
        dto.setCategoryName(product.getCategory().getCategoryName());
        dto.setDistrictName(product.getDistrict().getDistrictName());
        dto.setHasVariants(product.isHasVariants());
        List<ProductVariants> variants = product.getProductVariants();
        List<ProductVariantDetailResponseDTO> variantsList = new ArrayList<>();
        if (variants != null && !variants.isEmpty()) {
            double min_Price = Double.MAX_VALUE;
            int totalStock = 0;
            for (ProductVariants v : variants) {
                if (min_Price > v.getPrice()) {
                    min_Price = v.getPrice();
                }
                totalStock += v.getStock();
            }
            dto.setProductPrice(min_Price);
            dto.setStock(totalStock);

            for (ProductVariants v : variants) {
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

