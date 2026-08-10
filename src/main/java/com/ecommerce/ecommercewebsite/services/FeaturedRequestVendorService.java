package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.FeaturedRequestDTO;
import com.ecommerce.ecommercewebsite.dto.FeaturedRequestVendorResponseDTO;
import com.ecommerce.ecommercewebsite.dto.VendorFeaturedRequestPaymentResponseDTO;
import org.springframework.data.domain.Page;

import java.security.Principal;

public interface FeaturedRequestVendorService {
    public String requestFeaturedProduct(Long id, String email, FeaturedRequestDTO request);

    public Page<FeaturedRequestVendorResponseDTO> getMYFeaturedRequests(String email, int size, int page);

    public VendorFeaturedRequestPaymentResponseDTO getFeaturedRequestDetails(Long id, String email);

}
