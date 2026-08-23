package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.AddToCartRequestDTO;
import com.ecommerce.ecommercewebsite.dto.AddToCartResponseDTO;
import com.ecommerce.ecommercewebsite.dto.UpdateCartRequestDTO;
import com.ecommerce.ecommercewebsite.dto.UpdateCartResponseDTO;
import com.ecommerce.ecommercewebsite.dto.users.CartResponseDTO;
import com.ecommerce.ecommercewebsite.model.Cart;
import com.ecommerce.ecommercewebsite.model.User;

import java.security.Principal;
import java.util.List;

public interface UserService {
    public AddToCartResponseDTO addToCart(String email, AddToCartRequestDTO addToCartRequestDTO);

    public CartResponseDTO getCart(String email);

    public CartResponseDTO updateCart(Long cartItemId, String email, UpdateCartRequestDTO updateCartRequestDTO);

    public CartResponseDTO removeCartItem(Long cartItemId, Principal principal);

    public String clearAllCartItems(Long cartId, Principal principal);
}
