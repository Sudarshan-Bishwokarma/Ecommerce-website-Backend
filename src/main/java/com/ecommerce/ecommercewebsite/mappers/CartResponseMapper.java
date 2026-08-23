package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.AddToCartResponseDTO;
import com.ecommerce.ecommercewebsite.dto.users.CartResponseDTO;
import com.ecommerce.ecommercewebsite.model.Cart;
import com.ecommerce.ecommercewebsite.model.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class CartResponseMapper {
    public CartResponseDTO mapToDTO(Cart cart) {
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setCartId(cart.getId());
        List<CartItem> cartItems = cart.getItems();
        List<AddToCartResponseDTO> cartItemResponseDTOs = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            AddToCartResponseDTO dto = new AddToCartResponseDTO();
            dto.setCartItemId(cartItem.getId());
            dto.setProductId(cartItem.getProduct().getProductId());
            dto.setProductName(cartItem.getProduct().getProductName());
            dto.setQuantity(cartItem.getQuantity());
            dto.setTotalPrice(cartItem.getTotalPrice());
            if (cartItem.getProductVariant() != null) {
                dto.setProductPrice(cartItem.getProductVariant().getPrice());
                dto.setSize(cartItem.getProductVariant().getSize());
                dto.setColor(cartItem.getProductVariant().getColor());
                dto.setProductVariantId(cartItem.getProductVariant().getId());
                String base64 = Base64.getEncoder().encodeToString(cartItem.getProductVariant().getImage());
                dto.setProductImage(base64);
                dto.setAvailableStock(cartItem.getProductVariant().getStock());
            } else {
                dto.setProductPrice(cartItem.getProduct().getPrice());
                dto.setAvailableStock(cartItem.getProduct().getStock());
                dto.setColor(null);
                dto.setSize(null);
                String base64 = Base64.getEncoder().encodeToString(cartItem.getProduct().getProductImage());
                dto.setProductImage(base64);

            }
            subtotal = subtotal.add(cartItem.getTotalPrice());
            cartItemResponseDTOs.add(dto);
        }
        cartResponseDTO.setItems(cartItemResponseDTOs);


        cartResponseDTO.setSubtotal(subtotal);
        return cartResponseDTO;
    }
}
