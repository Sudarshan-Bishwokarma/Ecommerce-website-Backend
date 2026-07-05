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
        responseDTO.setProductVariantId(cartItem.getProductVariant().getId());
        responseDTO.setProductId(cartItem.getProductVariant().getProduct().getProductId());
        responseDTO.setProductName(cartItem.getProductVariant().getProduct().getProductName());
        responseDTO.setQuantity(cartItem.getQuantity());
        responseDTO.setSize(cartItem.getProductVariant().getSize());
        responseDTO.setColor((cartItem.getProductVariant().getColor()));
        responseDTO.setProductPrice(cartItem.getProductVariant().getPrice());
        responseDTO.setTotalPrice(cartItem.getTotalPrice());

        return responseDTO;
    }
}
