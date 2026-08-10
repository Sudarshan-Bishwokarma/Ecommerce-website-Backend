package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.FeaturedProductResponseDTO;
import com.ecommerce.ecommercewebsite.dto.FeaturedRequestActionDTO;
import org.springframework.data.domain.Page;

public interface FeaturedRequestAdminService {
    public Page<FeaturedProductResponseDTO> getPendingFeaturedRequests(int page, int size);

    public String approvedFeaturedRequest(Long id, FeaturedRequestActionDTO featuredRequest);

    public String rejectFeaturedRequest(Long id, FeaturedRequestActionDTO featuredRequest);
}
