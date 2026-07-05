package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.AddToCartRequestDTO;
import com.ecommerce.ecommercewebsite.dto.AddToCartResponseDTO;
import com.ecommerce.ecommercewebsite.dto.UpdateCartRequestDTO;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.exception.CartNotFoundException;
import com.ecommerce.ecommercewebsite.exception.ProductNotFoundException;
import com.ecommerce.ecommercewebsite.exception.UserNotFoundException;
import com.ecommerce.ecommercewebsite.mappers.AddToCartMapper;
import com.ecommerce.ecommercewebsite.model.*;
import com.ecommerce.ecommercewebsite.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    ProductVariantsRepository productVariantsRepository;
    @Autowired
    AddToCartMapper addToCartMapper;

    @Override
    public AddToCartResponseDTO addToCart(String email, AddToCartRequestDTO addToCartRequestDTO) {
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        //  get or  create a   cart
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                            Cart newCart = new Cart();
                            newCart.setUser(user);
                            return cartRepository.save(newCart);
                        }

                );
        //   get    product
        ProductVariants variant = productVariantsRepository.findById(addToCartRequestDTO.getProductVariantId())
                .orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_VARIANTS_NOT_FOUND));
        //  check if the   product  already exist in the cart
        CartItem cartItem = cartItemRepository.findByCartAndProductVariant(cart, variant).orElse(null);
        if (cartItem == null) {
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProductVariant(variant);
            newCartItem.setQuantity(addToCartRequestDTO.getQuantity());
            newCartItem.setTotalPrice(addToCartRequestDTO.getQuantity() * variant.getPrice());
            cartItemRepository.save(newCartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + addToCartRequestDTO.getQuantity());
            cartItem.setTotalPrice(addToCartRequestDTO.getQuantity() * variant.getPrice());
            cartItemRepository.save(cartItem);
        }

        System.out.println("Products added to cart");
        AddToCartResponseDTO responseDTO = addToCartMapper.mapToDTO(cartItem);
        return responseDTO;
    }

    @Override
    public List<AddToCartResponseDTO> getCart(User user) {
        Cart cart = cartRepository.findByUser(user).orElse(null);
        if (cart == null) {
            return new ArrayList<>();
        }
        List<CartItem> cartItems = cartItemRepository.findByCart(cart);
        if (cartItems.isEmpty()) {
            return new ArrayList<>();
        }
        List<AddToCartResponseDTO> dtos = new ArrayList<>();
        for (CartItem item : cartItems) {
            AddToCartResponseDTO dto = addToCartMapper.mapToDTO(item);
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public String updateCart(Long CartItemId, String email, UpdateCartRequestDTO updateCartRequestDTO) {
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        CartItem cartItem = cartItemRepository.findById(CartItemId).
                orElseThrow(() -> new ApiException(ProductErrorCode.CART_ITEM_NOT_FOUND));
        // ownership check
        if (cartItem.getCart().getUser().getId() != user.getId()) {
            throw new AccessDeniedException("Access denied");
        }
        if (updateCartRequestDTO.getQuantity() <= 0) {
            cartItemRepository.delete(cartItem);
            return " Cart Item deleted successfully";
        } else {
            cartItem.setQuantity(updateCartRequestDTO.getQuantity());
            cartItem.setTotalPrice(cartItem.getTotalPrice() * updateCartRequestDTO.getQuantity());
            cartItemRepository.save(cartItem);
            return "CartItem Successfully Updated";
        }

    }

    @Override
    public String removeCartItem(Long cartItemId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).
                orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        CartItem cartItem = cartItemRepository.findById(cartItemId).
                orElseThrow(() -> new CartNotFoundException("CartItem not found"));
        // check   cart ownership
        if (cartItem.getCart().getUser().getId() != user.getId()) {
            throw new AccessDeniedException("Access denied");
        }
        cartItemRepository.delete(cartItem); //no  return needed
        System.out.println("Cart Item deleted successfully");

        return "Cart Item Deleted Successfully";
    }

    @Override
    public String clearAllCartItems(Long cartId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        // check  ownerShip
        if (cart.getUser().getId() != user.getId()) {
            throw new AccessDeniedException("Access denied");
        }
        cart.getItems().clear();
        cartRepository.save(cart);
        System.out.println("Cart Items deleted successfully");

        return "Cart Item Deleted Successfully";
    }


}

