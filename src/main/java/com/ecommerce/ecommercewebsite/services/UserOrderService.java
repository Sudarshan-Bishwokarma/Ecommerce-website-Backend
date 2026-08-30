package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.OrderRequestDTO;
import com.ecommerce.ecommercewebsite.dto.OrderResponseDTO;
import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import org.springframework.data.domain.Page;

public interface UserOrderService {
    public OrderResponseDTO placeOrder(String email, OrderRequestDTO orderRequestDTO);

    public Page<OrderResponseDTO> getUsersOrders(String email, String sort, OrderStatus status, int page, int size);

    public String cancelOrder(Long orderId, String email);

    public OrderResponseDTO getOrderById(Long orderId, String email);
}
