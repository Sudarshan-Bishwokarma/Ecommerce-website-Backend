package com.ecommerce.ecommercewebsite.services.users;

import com.ecommerce.ecommercewebsite.dto.UserFeaturedProductResponseDTO;
import com.ecommerce.ecommercewebsite.dto.UserLatestProductResponseDTO;
import com.ecommerce.ecommercewebsite.enums.ProductStatus;
import com.ecommerce.ecommercewebsite.mappers.LatestProductMapper;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.repositories.ProductRepository;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LatestProductServiceImpl implements LatestProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private LatestProductMapper latestProductMapper;

    @Override
    public List<UserLatestProductResponseDTO> getLatestProducts() {
        List<Product> products = productRepository.findTop8ByStatusOrderByProductIdDesc(ProductStatus.ACTIVE);
        List<UserLatestProductResponseDTO> userLatestProductResponseDTOS = new ArrayList<>();
        for (Product product : products) {
            UserLatestProductResponseDTO userLatestProductResponseDTO = latestProductMapper.mapToDTO(product);
            userLatestProductResponseDTOS.add(userLatestProductResponseDTO);
        }
        return userLatestProductResponseDTOS;

    }
}
