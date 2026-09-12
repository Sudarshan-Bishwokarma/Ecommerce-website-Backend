package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.superadmin.SuperAdminFeaturedRequestDetailsResponseDTO;
import com.ecommerce.ecommercewebsite.model.FeaturedRequest;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class FeaturedRequestDetailsMapper {
    public SuperAdminFeaturedRequestDetailsResponseDTO mapToFeaturedRequestDetailsResponseDTO(FeaturedRequest featuredRequest) {

        SuperAdminFeaturedRequestDetailsResponseDTO responseDTO = new SuperAdminFeaturedRequestDetailsResponseDTO();
        // Request information
        responseDTO.setRequestId(featuredRequest.getId());
        responseDTO.setStatus(featuredRequest.getStatus());
        responseDTO.setAdminMessage(featuredRequest.getAdminMessage());
        responseDTO.setStartDate(featuredRequest.getStartDate());
        responseDTO.setEndDate(featuredRequest.getEndDate());
        responseDTO.setTransactionUuid(featuredRequest.getTransactionUuid());

        // Product information
        if (featuredRequest.getProduct() != null) {
            responseDTO.setProductId(featuredRequest.getProduct().getProductId());

            responseDTO.setProductName(featuredRequest.getProduct().getProductName());

            responseDTO.setProductDescription(featuredRequest.getProduct().getProductDescription());

            responseDTO.setHasVariants(featuredRequest.getProduct().isHasVariants());

            if (featuredRequest.getProduct().getProductImage() != null) {
                String base64Image = Base64.getEncoder().encodeToString(
                        featuredRequest.getProduct().getProductImage()
                );

                responseDTO.setProductImage(base64Image);
            }

            // Category
            if (featuredRequest.getProduct().getCategory() != null) {
                responseDTO.setCategoryName(featuredRequest.getProduct().getCategory().getCategoryName());
            }

            // District
            if (featuredRequest.getProduct().getDistrict() != null) {
                responseDTO.setDistrictName(featuredRequest.getProduct().getDistrict().getDistrictName());
            }
        }

        // Vendor information
        if (featuredRequest.getVendor() != null) {

            responseDTO.setVendorId(featuredRequest.getVendor().getId());

            responseDTO.setVendorName(featuredRequest.getVendor().getName());

            responseDTO.setVendorEmail(featuredRequest.getVendor().getEmail());

            if (featuredRequest.getVendor().getProfile() != null) {
                responseDTO.setVendorPhone(featuredRequest.getVendor().getProfile().getNumber());
            }

            if (featuredRequest.getVendor().getBusinessProfile() != null) {

                responseDTO.setBusinessName(featuredRequest.getVendor().getBusinessProfile().getBusinessName());

                responseDTO.setBusinessEmail(featuredRequest.getVendor().getBusinessProfile().getBusinessEmail());

                responseDTO.setBusinessPhone(featuredRequest.getVendor().getBusinessProfile().getBusinessPhone());

                responseDTO.setBusinessAddress(featuredRequest.getVendor().getBusinessProfile().getBusinessAddress());

                responseDTO.setBusinessWebsite(featuredRequest.getVendor().getBusinessProfile().getBusinessWebsite());
            }
        }

        // Featured Plan information
        if (featuredRequest.getFeaturedPlan() != null) {
            responseDTO.setFeaturedPlanId(featuredRequest.getFeaturedPlan().getId());

            responseDTO.setFeaturedPlanName(featuredRequest.getFeaturedPlan().getName());

            responseDTO.setDurationDays(featuredRequest.getFeaturedPlan().getDurationDays());

            responseDTO.setPrice(featuredRequest.getFeaturedPlan().getPrice());

            responseDTO.setFeaturePlanType(featuredRequest.getFeaturedPlan().getFeaturePlanType());
        }

        return responseDTO;
    }
}
