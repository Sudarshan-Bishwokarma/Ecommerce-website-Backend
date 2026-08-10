package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.FeaturedRequestDTO;
import com.ecommerce.ecommercewebsite.dto.FeaturedRequestVendorResponseDTO;
import com.ecommerce.ecommercewebsite.dto.VendorFeaturedRequestPaymentResponseDTO;
import com.ecommerce.ecommercewebsite.enums.*;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.mappers.FeaturedRequestMapper;
import com.ecommerce.ecommercewebsite.mappers.VendorFeaturedRequestMapper;
import com.ecommerce.ecommercewebsite.model.FeaturedPlan;
import com.ecommerce.ecommercewebsite.model.FeaturedRequest;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.FeaturedPlanRepository;
import com.ecommerce.ecommercewebsite.repositories.FeaturedRequestRepository;
import com.ecommerce.ecommercewebsite.repositories.ProductRepository;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FeaturedRequestVendorServiceImpl implements FeaturedRequestVendorService {
    @Autowired
    private FeaturedRequestRepository featuredRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FeaturedRequestMapper featuredRequestMapper;
    @Autowired
    private VendorFeaturedRequestMapper vendorFeaturedRequestMapper;
    @Autowired
    private FeaturedPlanRepository featuredPlanRepository;

    @Override
    public String requestFeaturedProduct(Long id, String email, FeaturedRequestDTO request) {
        User vendor = userRepository.findByEmail(email).orElseThrow((() -> new ApiException(AuthErrorCode.VENDOR_NOT_FOUND)));
        Product product = productRepository.findByProductIdAndVendor(id, vendor).orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
        // only active  products can  request for  featured plan
        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new ApiException(ProductErrorCode.INVALID_PRODUCT_STATUS);
        }
        FeaturedPlan plan = featuredPlanRepository.findById(request.getFeaturedPlanId()).orElseThrow(() -> new ApiException(FeaturedPlanErrorCode.FEATURED_PLAN_NOT_FOUND));
        FeaturedRequest existingRequest = featuredRequestRepository.findTopByProductOrderByIdDesc(product);
        if (existingRequest != null) {

            FeaturedRequestStatus status = existingRequest.getStatus();

            if (status == FeaturedRequestStatus.APPROVED || status == FeaturedRequestStatus.PENDING) {
                throw new ApiException(ProductErrorCode.REQUEST_ALREADY_EXISTS);
            }
            if (status == FeaturedRequestStatus.PAID) {

                if (existingRequest.getEndDate() == null || existingRequest.getEndDate().isAfter(LocalDateTime.now())) {
                    throw new ApiException(ProductErrorCode.PRODUCT_ALREADY_FEATURED);
                }
            }
        }
        if (!plan.isActive()) {
            throw new ApiException(FeaturedPlanErrorCode.FEATURED_PLAN_NOT_FOUND);

        }
        FeaturedRequest featuredRequest = new FeaturedRequest();
        featuredRequest.setProduct(product);
        featuredRequest.setVendor(vendor);
        featuredRequest.setFeaturedPlan(plan);
        featuredRequest.setStatus(FeaturedRequestStatus.PENDING);
        featuredRequestRepository.save(featuredRequest);
        return "Featured request submitted successfully";
    }

    @Override
    public Page<FeaturedRequestVendorResponseDTO> getMYFeaturedRequests(String email, int page, int size) {
        User vendor = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.VENDOR_NOT_FOUND));
        Pageable pageable = PageRequest.of(page, size);
        Page<FeaturedRequest> featuredRequests = featuredRequestRepository.findByVendor(vendor, pageable);
        return featuredRequests.map(vendorFeaturedRequestMapper::mapToDTO);
    }

    @Override
    public VendorFeaturedRequestPaymentResponseDTO getFeaturedRequestDetails(Long id, String email) {
        User vendor = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.VENDOR_NOT_FOUND));
        FeaturedRequest request = featuredRequestRepository.findById(id).orElseThrow(() -> new ApiException(ProductErrorCode.FEATURED_PRODUCT_REQUEST_NOT_FOUND));
        VendorFeaturedRequestPaymentResponseDTO dto = new VendorFeaturedRequestPaymentResponseDTO();
        dto.setFeaturedRequestId(request.getId());
        dto.setFeaturedPlanId(request.getFeaturedPlan().getId());
        dto.setProductName(request.getProduct().getProductName());
        dto.setFeaturedPlanName(request.getFeaturedPlan().getName());
        dto.setDurationDays(request.getFeaturedPlan().getDurationDays());
        dto.setPrice(request.getFeaturedPlan().getPrice());
        dto.setStatus(request.getStatus());
        return dto;
    }


}
