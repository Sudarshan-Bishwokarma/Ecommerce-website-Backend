package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.UserLatestProductResponseDTO;
import com.ecommerce.ecommercewebsite.model.Product;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class LatestProductMapper {
    public UserLatestProductResponseDTO mapToDTO(Product product) {
        UserLatestProductResponseDTO dto = new UserLatestProductResponseDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        if (product.getProductImage() != null) {
            String base64 = Base64.getEncoder().encodeToString(product.getProductImage());
            dto.setProductImage(base64);
        }
        dto.setPrice(product.getDisplayPrice());
        dto.setVendorName(product.getVendor().getName());
        dto.setDistrictName(product.getDistrict().getDistrictName());
        return dto;

    }
}
