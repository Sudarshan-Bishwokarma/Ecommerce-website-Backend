package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.AddToCartRequestDTO;
import com.ecommerce.ecommercewebsite.dto.AddToCartResponseDTO;
import com.ecommerce.ecommercewebsite.dto.UpdateCartRequestDTO;
import com.ecommerce.ecommercewebsite.dto.UpdateCartResponseDTO;
import com.ecommerce.ecommercewebsite.dto.users.CartResponseDTO;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.exception.CartNotFoundException;
import com.ecommerce.ecommercewebsite.mappers.AddToCartMapper;
import com.ecommerce.ecommercewebsite.mappers.CartResponseMapper;
import com.ecommerce.ecommercewebsite.mappers.UpdateCartMapper;
import com.ecommerce.ecommercewebsite.model.*;
import com.ecommerce.ecommercewebsite.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.context.request.RequestAttributes;


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
    @Autowired
    UpdateCartMapper updateCartMapper;
    @Autowired
    CartResponseMapper cartResponseMapper;


    @Override
    public AddToCartResponseDTO addToCart(String email, AddToCartRequestDTO addToCartRequestDTO) {
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        //  get or  create a  cart
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                            Cart newCart = new Cart();
                            newCart.setUser(user);
                            return cartRepository.save(newCart);
                        }

                );
        // Get product
        Product product = productRepository.findById(addToCartRequestDTO.getProductId())
                .orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_NOT_FOUND));
        ProductVariant variant = null;
        Integer availableStock;
        Integer newQuantity = addToCartRequestDTO.getQuantity();


        BigDecimal price;
        if (addToCartRequestDTO.getProductVariantId() != null) {
            //   get    product  variant
            variant = productVariantsRepository.findById(addToCartRequestDTO.getProductVariantId())
                    .orElseThrow(() -> new ApiException(ProductErrorCode.PRODUCT_VARIANTS_NOT_FOUND));
            price = variant.getPrice();
            availableStock = variant.getStock();

        } else {

            price = product.getPrice();
            availableStock = product.getStock();

        }

        //  check if the   product variant  already exist in the cartItem
        CartItem cartItem = cartItemRepository.findByCartAndProductAndProductVariant(cart, product, variant).orElse(null);


        if (cartItem == null) {
            //   first time adding the  product
            if (newQuantity > availableStock) {
                throw new ApiException(ProductErrorCode.INSUFFICIENT_STOCK_AVAILABLE);
            }
            CartItem newCartItem = new CartItem();
            newCartItem.setCart(cart);
            newCartItem.setProduct(product);
            newCartItem.setProductVariant(variant);
            newCartItem.setQuantity(newQuantity);
            newCartItem.setTotalPrice(price.multiply(BigDecimal.valueOf(newQuantity)));
            cartItem = cartItemRepository.save(newCartItem);
        } else {
            //  if  product  variant   exist  update quantity
            Integer updatedQuantity = cartItem.getQuantity() + newQuantity;
            if (updatedQuantity > availableStock) {
                throw new ApiException(ProductErrorCode.INSUFFICIENT_STOCK_AVAILABLE);
            }
            cartItem.setQuantity(updatedQuantity);
            cartItem.setTotalPrice(price.multiply(BigDecimal.valueOf(updatedQuantity)));

            cartItemRepository.save(cartItem);
        }

        System.out.println("Products added to cart");
        AddToCartResponseDTO responseDTO = addToCartMapper.mapToDTO(cartItem);
        return responseDTO;
    }

    //  get cart item
    @Override
    public CartResponseDTO getCart(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));

        Cart cart = cartRepository.findByUser(user).orElse(null);

        // User has no cart yet
        if (cart == null) {

            CartResponseDTO emptyCart = new CartResponseDTO();

            emptyCart.setCartId(null);
            emptyCart.setItems(new ArrayList<>());
            emptyCart.setSubtotal(BigDecimal.ZERO);

            return emptyCart;
        }

        return cartResponseMapper.mapToDTO(cart);
    }

    @Override
    public CartResponseDTO updateCart(Long CartItemId, String email, UpdateCartRequestDTO updateCartRequestDTO) {
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        CartItem cartItem = cartItemRepository.findById(CartItemId).
                orElseThrow(() -> new ApiException(ProductErrorCode.CART_ITEM_NOT_FOUND));
        // ownership check
        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied");
        }
        if (updateCartRequestDTO.getQuantity() <= 0) {
            throw new ApiException(ProductErrorCode.QUANTITY_NOT_ENOUGH);
        }
        cartItem.setQuantity(updateCartRequestDTO.getQuantity());
        BigDecimal totalPrice = null;
        if (cartItem.getProductVariant() != null) {
            totalPrice = cartItem.getProductVariant().getPrice().multiply(BigDecimal.valueOf(updateCartRequestDTO.getQuantity()));
        } else {
            totalPrice = cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(updateCartRequestDTO.getQuantity()));
        }
        cartItem.setTotalPrice(totalPrice);
        CartItem savedCartItem = cartItemRepository.save(cartItem);


        CartResponseDTO responseDTO = cartResponseMapper.mapToDTO(savedCartItem.getCart());
        return responseDTO;
    }

    @Override
    public CartResponseDTO removeCartItem(Long cartItemId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName()).
                orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        CartItem cartItem = cartItemRepository.findById(cartItemId).
                orElseThrow(() -> new CartNotFoundException("CartItem not found"));
        // check   cart ownership
        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Access denied");
        }
        cartItemRepository.delete(cartItem);
        Cart updatedCart = cartRepository.findByUser(user).orElseThrow(() -> new ApiException(ProductErrorCode.CART_ITEM_NOT_FOUND));

        CartResponseDTO response = cartResponseMapper.mapToDTO(updatedCart);
        return response;
    }

    @Override
    public String clearAllCartItems(Long cartId, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(ProductErrorCode.CART_NOT_FOUND));

        cart.getItems().clear();
        cartRepository.save(cart);
        System.out.println("Cart Items deleted successfully");

        return "Cart Item Deleted Successfully";
    }


}

