package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.FeaturedRequestVendorResponseDTO;
import com.ecommerce.ecommercewebsite.model.FeaturedRequest;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class VendorFeaturedRequestMapper {
    public FeaturedRequestVendorResponseDTO mapToDTO(FeaturedRequest featuredRequest) {
        FeaturedRequestVendorResponseDTO dto = new FeaturedRequestVendorResponseDTO();
        dto.setRequestId(featuredRequest.getId());
        dto.setStatus(featuredRequest.getStatus());
        dto.setAdminMessage(featuredRequest.getAdminMessage());
        dto.setProductId(featuredRequest.getProduct().getProductId());
        dto.setProductName(featuredRequest.getProduct().getProductName());
        if (featuredRequest.getProduct().getProductImage() != null) {
            String base64 = Base64.getEncoder().encodeToString(featuredRequest.getProduct().getProductImage());
            dto.setProductImage(base64);
        }
        return dto;


    }

}
