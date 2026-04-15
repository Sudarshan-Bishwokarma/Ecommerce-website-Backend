package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.OrderDetailDTO;
import com.ecommerce.ecommercewebsite.dto.OrderResponseDTO;
import com.ecommerce.ecommercewebsite.dto.UpdateOrderStatusDTO;
import com.ecommerce.ecommercewebsite.dto.UpdateOrderStatusResponseDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface AdminOrderService {
    public Page<OrderResponseDTO> getAllUsersOrders(int page, int size);

    public OrderDetailDTO getMoreOrderDetails(Long orderId);

    public UpdateOrderStatusResponseDTO updateOrderStatus(Long orderId, UpdateOrderStatusDTO updateOrderStatusDTO);

    public Page<OrderResponseDTO> getOrderByStatus(String status, int page, int size);

    public Page<OrderResponseDTO> getOrdersByDate(LocalDateTime startDate, LocalDateTime endDate, int page, int size);

    public Page<OrderResponseDTO> getOrderByEmail(String email, int page, int size);
}
