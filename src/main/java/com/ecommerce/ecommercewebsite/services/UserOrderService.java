package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.OrderRequestDTO;
import com.ecommerce.ecommercewebsite.dto.OrderResponseDTO;
import org.springframework.data.domain.Page;

public interface UserOrderService {
    public OrderResponseDTO placeOrder(String email, OrderRequestDTO orderRequestDTO);

    public Page<OrderResponseDTO> getUsersOrders(String email, int page, int size);

    public String cancelOrder(Long orderId, String email);

    public OrderResponseDTO getOrderById(Long orderId, String email);
}
