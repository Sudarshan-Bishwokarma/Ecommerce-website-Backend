package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.AddToCartRequestDTO;
import com.ecommerce.ecommercewebsite.dto.AddToCartResponseDTO;
import com.ecommerce.ecommercewebsite.exception.ProductNotFoundException;
import com.ecommerce.ecommercewebsite.exception.UserNotFoundException;
import com.ecommerce.ecommercewebsite.model.Cart;
import com.ecommerce.ecommercewebsite.model.CartItem;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.CartItemRepository;
import com.ecommerce.ecommercewebsite.repositories.CartRepository;
import com.ecommerce.ecommercewebsite.repositories.ProductRepository;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public AddToCartResponseDTO addToCart(String email, AddToCartRequestDTO addToCartRequestDTO) {
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new UserNotFoundException("User not found"));
        //  get or  create a   cart
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                            Cart newCart = new Cart();
                            newCart.setUser(user);
                            return cartRepository.save(newCart);
                        }

                );
        //   get    product
        Product product = productRepository.findById(addToCartRequestDTO.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        //  check if the   product  already exist in the cart
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + addToCartRequestDTO.getQuantity());
            cartItem.setTotalPrice(cartItem.getQuantity() * product.getProductPrice());
            cartItemRepository.save(cartItem);
        } else {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setQuantity(addToCartRequestDTO.getQuantity());
            cartItem.setProduct(product);
            cartItem.setTotalPrice(addToCartRequestDTO.getQuantity() * product.getProductPrice());
            cartItemRepository.save(cartItem);
        }

        System.out.println("Product added to cart");
        AddToCartResponseDTO responseDTO = mapToDTO(cartItem);
        return responseDTO;
    }

    @Override
    public List<AddToCartResponseDTO> getCart(User user) {
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        List<CartItem> cartItem = cartItemRepository.findByCart(cart);

        if (cartItem.isEmpty()) {
            return new ArrayList<>();
        }
        List<AddToCartResponseDTO> dtos = new ArrayList<>();
        for (CartItem item : cartItem) {
            AddToCartResponseDTO dto = mapToDTO(item);
            dtos.add(dto);
        }

        return dtos;
    }

    // helper class
    private AddToCartResponseDTO mapToDTO(CartItem cartItem) {
        AddToCartResponseDTO responseDTO = new AddToCartResponseDTO();
        responseDTO.setCartItemId(cartItem.getId());
        responseDTO.setProductId(cartItem.getProduct().getProductId());
        responseDTO.setProductName(cartItem.getProduct().getProductName());
        responseDTO.setProductPrice(cartItem.getProduct().getProductPrice());
        responseDTO.setTotalPrice(cartItem.getTotalPrice());
        responseDTO.setQuantity(cartItem.getQuantity());
        return responseDTO;
    }

}
