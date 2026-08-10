package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.UserFeaturedProductResponseDTO;
import com.ecommerce.ecommercewebsite.model.FeaturedRequest;
import com.ecommerce.ecommercewebsite.model.ProductVariant;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

@Component
public class UserFeaturedProductMapper {
    public UserFeaturedProductResponseDTO mapToDTO(FeaturedRequest featuredRequest) {
        UserFeaturedProductResponseDTO dto = new UserFeaturedProductResponseDTO();
        dto.setProductId(featuredRequest.getProduct().getProductId());
        dto.setProductName(featuredRequest.getProduct().getProductName());
        if (featuredRequest.getProduct().getProductImage() != null) {
            String base64 = Base64.getEncoder().encodeToString(featuredRequest.getProduct().getProductImage());
            dto.setProductImage(base64);
        }

        List<ProductVariant> allVariant = featuredRequest.getProduct().getProductVariants();
        if (allVariant != null && !allVariant.isEmpty()) {
            BigDecimal displayPrice = null;
            for (ProductVariant variant : allVariant) {
                if (variant.getPrice() != null &&
                        (displayPrice == null || variant.getPrice().compareTo(displayPrice) < 0)) {

                    displayPrice = variant.getPrice();
                }
            }
            dto.setPrice(displayPrice); // min   price for variant

        } else {
            dto.setPrice(featuredRequest.getProduct().getPrice()); //   product  main  price
        }
        dto.setVendorName(featuredRequest.getVendor().getName());
        dto.setDistrictName(featuredRequest.getProduct().getDistrict().getDistrictName());
        return dto;
    }
}
