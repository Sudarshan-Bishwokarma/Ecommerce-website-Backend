package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.AddToCartRequestDTO;
import com.ecommerce.ecommercewebsite.dto.AddToCartResponseDTO;
import com.ecommerce.ecommercewebsite.dto.UpdateCartRequestDTO;
import com.ecommerce.ecommercewebsite.exception.CartNotFoundException;
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

    @Override
    public String updateCart(Long CartItemId, String email, UpdateCartRequestDTO updateCartRequestDTO) {
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new UserNotFoundException("User not found"));
        CartItem cartItem = cartItemRepository.findById(CartItemId).
                orElseThrow(() -> new CartNotFoundException("CartItem not found"));
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
            return "CertItem Successfully Updated";
        }

    }

    @Override
    public String removeCartItem(Long cartItemId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).
                orElseThrow(() -> new UserNotFoundException("User not found"));
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
                .orElseThrow(() -> new UserNotFoundException("User not found"));
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

/*
Even if a user is logged in, we still check the database because login only tells us who the user is, not
 the user still exists or is allowed to do actions. The database check makes sure the user is valid before doing business work like adding items to the cart.
 */
/*
gged-in user adds a product, we find the user and their cart, update the product quantity if it’s already there,
 or add it as a new item, then save.
 */