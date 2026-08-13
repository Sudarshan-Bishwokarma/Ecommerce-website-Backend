package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.AddToCartRequestDTO;
import com.ecommerce.ecommercewebsite.dto.AddToCartResponseDTO;
import com.ecommerce.ecommercewebsite.model.CartItem;
import org.springframework.stereotype.Component;

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
        } else {
            responseDTO.setProductVariantId(null);
            responseDTO.setSize(null);
            responseDTO.setColor(null);
            responseDTO.setProductPrice(cartItem.getProduct().getPrice());
        }
        responseDTO.setQuantity(cartItem.getQuantity());
        responseDTO.setTotalPrice(cartItem.getTotalPrice());

        return responseDTO;
    }
}
