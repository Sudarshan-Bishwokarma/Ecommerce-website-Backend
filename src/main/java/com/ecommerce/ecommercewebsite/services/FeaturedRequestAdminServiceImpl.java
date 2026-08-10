package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.FeaturedProductResponseDTO;
import com.ecommerce.ecommercewebsite.dto.FeaturedRequestActionDTO;
import com.ecommerce.ecommercewebsite.enums.FeaturedRequestStatus;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.mappers.FeaturedRequestMapper;
import com.ecommerce.ecommercewebsite.model.FeaturedRequest;
import com.ecommerce.ecommercewebsite.repositories.FeaturedRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FeaturedRequestAdminServiceImpl implements FeaturedRequestAdminService {
    @Autowired
    private FeaturedRequestMapper featuredRequestMapper;
    @Autowired
    private FeaturedRequestRepository featuredRequestRepository;

    @Override
    public Page<FeaturedProductResponseDTO> getPendingFeaturedRequests(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FeaturedRequest> allPendingFeaturedRequests = featuredRequestRepository.findByStatus(FeaturedRequestStatus.PENDING, pageable);
        return allPendingFeaturedRequests.map(featuredRequestMapper::mapToFeaturedProductResponseDTO);
    }

    @Override
    public String approvedFeaturedRequest(Long id, FeaturedRequestActionDTO featuredRequestActionDTO) {
        FeaturedRequest request = featuredRequestRepository.findById(id).orElseThrow(() -> new ApiException(ProductErrorCode.FEATURED_PRODUCT_REQUEST_NOT_FOUND));
        if (request.getStatus() != FeaturedRequestStatus.PENDING) {
            throw new ApiException(ProductErrorCode.INVALID_PRODUCT_STATUS);
        }
        request.setStatus(FeaturedRequestStatus.APPROVED);
        request.setAdminMessage(featuredRequestActionDTO.getMessage());
        featuredRequestRepository.save(request);

        return "Featured request approved successfully";
    }

    @Override
    public String rejectFeaturedRequest(Long id, FeaturedRequestActionDTO requestActionDTO) {
        FeaturedRequest request = featuredRequestRepository.findById(id).orElseThrow(() -> new ApiException(ProductErrorCode.FEATURED_PRODUCT_REQUEST_NOT_FOUND));
        if (request.getStatus() != FeaturedRequestStatus.PENDING) {
            throw new ApiException(ProductErrorCode.INVALID_PRODUCT_STATUS);
        }
        request.setStatus(FeaturedRequestStatus.REJECTED);
        request.setAdminMessage(requestActionDTO.getMessage());
        featuredRequestRepository.save(request);
        return "Featured request rejected successfully";
    }
}
