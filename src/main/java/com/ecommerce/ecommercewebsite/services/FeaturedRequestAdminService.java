package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.FeaturedProductResponseDTO;
import com.ecommerce.ecommercewebsite.dto.FeaturedRequestActionDTO;
import com.ecommerce.ecommercewebsite.dto.superadmin.SuperAdminFeaturedRequestDetailsResponseDTO;
import com.ecommerce.ecommercewebsite.enums.FeaturedRequestStatus;
import org.springframework.data.domain.Page;

public interface FeaturedRequestAdminService {
    public SuperAdminFeaturedRequestDetailsResponseDTO getFeaturedRequestDetails(Long featuredRequestId);

    public Page<FeaturedProductResponseDTO> getPendingFeaturedRequests(int page, int size);

    public String approvedFeaturedRequest(Long id, FeaturedRequestActionDTO featuredRequest);

    public String rejectFeaturedRequest(Long id, FeaturedRequestActionDTO featuredRequest);

    public Page<FeaturedProductResponseDTO> getFeaturedRequests(FeaturedRequestStatus status, int page, int size);
}
