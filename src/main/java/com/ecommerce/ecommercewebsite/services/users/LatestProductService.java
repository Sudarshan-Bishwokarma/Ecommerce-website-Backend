package com.ecommerce.ecommercewebsite.services.users;

import com.ecommerce.ecommercewebsite.dto.UserFeaturedProductResponseDTO;
import com.ecommerce.ecommercewebsite.dto.UserLatestProductResponseDTO;

import java.util.List;

public interface LatestProductService {
    List<UserLatestProductResponseDTO> getLatestProducts();

}
