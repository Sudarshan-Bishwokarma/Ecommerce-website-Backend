package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.VendorProductDetailResponseDTO;
import com.ecommerce.ecommercewebsite.dto.users.ProductVariantDetailResponseDTO;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.model.ProductVariant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class VendorProductDetailsMapper {
    public VendorProductDetailResponseDTO mapToDTO(Product product) {
        VendorProductDetailResponseDTO dto = new VendorProductDetailResponseDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductDescription(product.getProductDescription());
        dto.setStatus(product.getStatus());
        if (product.getProductImage() != null) {
            String base64 = Base64.getEncoder().encodeToString(product.getProductImage());
            dto.setProductImageBase64(base64);
        }
        dto.setFeatured(product.isFeatured());
        dto.setCategoryId(product.getCategory().getId());
        dto.setCategoryName(product.getCategory().getCategoryName());
        dto.setDistrictId(product.getDistrict().getId());
        dto.setDistrictName(product.getDistrict().getDistrictName());
        dto.setHasVariants(product.isHasVariants());
        dto.setProductPrice(product.getDisplayPrice());
        List<ProductVariant> variants = product.getProductVariants();
        List<ProductVariantDetailResponseDTO> variantsList = new ArrayList<>();
        if (variants != null && !variants.isEmpty()) {
            int totalStock = 0;
            for (ProductVariant v : variants) {
                totalStock += v.getStock();
            }
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
            dto.setStock(product.getStock());
        }

        return dto;
    }
}

