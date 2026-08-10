package com.ecommerce.ecommercewebsite.services.users;

import com.ecommerce.ecommercewebsite.dto.UserFeaturedProductResponseDTO;

import java.util.List;

public interface FeaturedProductService {
    public List<UserFeaturedProductResponseDTO> getFeaturedProducts();
}
