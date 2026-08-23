package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.AddToCartRequestDTO;
import com.ecommerce.ecommercewebsite.dto.AddToCartResponseDTO;
import com.ecommerce.ecommercewebsite.model.CartItem;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class AddToCartMapper {
    public AddToCartResponseDTO mapToDTO(CartItem cartItem) {
        AddToCartResponseDTO responseDTO = new AddToCartResponseDTO();
        responseDTO.setCartItemId(cartItem.getId());
        responseDTO.setProductId(cartItem.getProduct().getProductId());
        responseDTO.setProductName(cartItem.getProduct().getProductName());

        if (cartItem.getProductVariant() != null) {
            responseDTO.setProductVariantId(cartItem.getProductVariant().getId());
            responseDTO.setSize(cartItem.getProductVariant().getSize());
            responseDTO.setColor((cartItem.getProductVariant().getColor()));
            responseDTO.setProductPrice(cartItem.getProductVariant().getPrice());
            String base64 = Base64.getEncoder().encodeToString(cartItem.getProductVariant().getImage());
            responseDTO.setProductImage(base64);
            responseDTO.setAvailableStock(cartItem.getProductVariant().getStock());
        } else {
            responseDTO.setProductVariantId(null);
            responseDTO.setSize(null);
            responseDTO.setColor(null);
            responseDTO.setProductPrice(cartItem.getProduct().getPrice());
            String base64 = Base64.getEncoder().encodeToString(cartItem.getProduct().getProductImage());
            responseDTO.setProductImage(base64);
            responseDTO.setAvailableStock(cartItem.getProduct().getStock());
        }
        responseDTO.setQuantity(cartItem.getQuantity());

        responseDTO.setTotalPrice(cartItem.getTotalPrice());

        return responseDTO;
    }
}
