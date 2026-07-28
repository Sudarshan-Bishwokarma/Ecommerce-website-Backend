package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.UpdateCartRequestDTO;
import com.ecommerce.ecommercewebsite.dto.UpdateCartResponseDTO;
import com.ecommerce.ecommercewebsite.model.CartItem;
import org.springframework.stereotype.Component;

@Component
public class UpdateCartMapper {
    public UpdateCartResponseDTO mapToDTO(CartItem item) {
        UpdateCartResponseDTO updateCartResponseDTO = new UpdateCartResponseDTO();
        updateCartResponseDTO.setCartItemId(item.getId());
        updateCartResponseDTO.setProductId(item.getProduct().getProductId());
        updateCartResponseDTO.setProductName(item.getProduct().getProductName());
        updateCartResponseDTO.setQuantity(item.getQuantity());
        updateCartResponseDTO.setTotalPrice(item.getTotalPrice());
        return updateCartResponseDTO;
    }
}
