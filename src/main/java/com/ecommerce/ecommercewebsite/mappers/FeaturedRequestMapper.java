package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.FeaturedProductResponseDTO;
import com.ecommerce.ecommercewebsite.model.FeaturedRequest;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class FeaturedRequestMapper {
    public FeaturedProductResponseDTO mapToFeaturedProductResponseDTO(FeaturedRequest featuredRequest) {
        FeaturedProductResponseDTO featuredProductResponseDTO = new FeaturedProductResponseDTO();
        featuredProductResponseDTO.setRequestId(featuredRequest.getId());
        featuredProductResponseDTO.setProductId(featuredRequest.getProduct().getProductId());
        featuredProductResponseDTO.setProductName(featuredRequest.getProduct().getProductName());
        if (featuredRequest.getProduct().getProductImage() != null) {
            String baseImage64 = Base64.getEncoder().encodeToString(featuredRequest.getProduct().getProductImage());
            featuredProductResponseDTO.setProductImage(baseImage64);
        }
        featuredProductResponseDTO.setProductDescription(featuredRequest.getProduct().getProductDescription());
        featuredProductResponseDTO.setVendorId(featuredRequest.getVendor().getId());
        featuredProductResponseDTO.setVendorName(featuredRequest.getVendor().getName());
        featuredProductResponseDTO.setCategoryName(featuredRequest.getProduct().getCategory().getCategoryName());
        featuredProductResponseDTO.setDistrictName(featuredRequest.getProduct().getDistrict().getDistrictName());
        featuredProductResponseDTO.setStatus(featuredRequest.getStatus());
        featuredProductResponseDTO.setAdminMessage(featuredRequest.getAdminMessage());
        return featuredProductResponseDTO;
    }
}
