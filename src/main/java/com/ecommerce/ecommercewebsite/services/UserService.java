package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.AddToCartRequestDTO;
import com.ecommerce.ecommercewebsite.dto.AddToCartResponseDTO;
import com.ecommerce.ecommercewebsite.model.User;

import java.security.Principal;
import java.util.List;

public interface UserService {
    public AddToCartResponseDTO addToCart(String email, AddToCartRequestDTO addToCartRequestDTO);

    public List<AddToCartResponseDTO> getCart(User user);
}
