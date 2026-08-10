package com.ecommerce.ecommercewebsite.services.users;

import com.ecommerce.ecommercewebsite.dto.UserFeaturedProductResponseDTO;
import com.ecommerce.ecommercewebsite.enums.FeaturedRequestStatus;
import com.ecommerce.ecommercewebsite.mappers.UserFeaturedProductMapper;
import com.ecommerce.ecommercewebsite.model.FeaturedRequest;
import com.ecommerce.ecommercewebsite.repositories.FeaturedRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeaturedProductServiceImpl implements FeaturedProductService {
    @Autowired
    private FeaturedRequestRepository featuredRequestRepository;
    @Autowired
    private UserFeaturedProductMapper userFeaturedProductMapper;

    @Override
    public List<UserFeaturedProductResponseDTO> getFeaturedProducts() {
        List<FeaturedRequest> featuredProductsRequests = featuredRequestRepository.findByStatus(FeaturedRequestStatus.PAID);
        List<UserFeaturedProductResponseDTO> userFeaturedProductResponseDTOS = new ArrayList<>();
        for (FeaturedRequest featuredRequest : featuredProductsRequests) {
            userFeaturedProductResponseDTOS.add(userFeaturedProductMapper.mapToDTO(featuredRequest));

        }
        return userFeaturedProductResponseDTOS;
    }
}
