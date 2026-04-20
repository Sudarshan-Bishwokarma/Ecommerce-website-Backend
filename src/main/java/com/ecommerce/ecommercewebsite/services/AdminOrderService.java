package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface AdminOrderService {
    public Page<OrderResponseDTO> getAllUsersOrders(int page, int size);

    public OrderDetailDTO getMoreOrderDetails(Long orderId);

    public UpdateOrderStatusResponseDTO updateOrderStatus(Long orderId, UpdateOrderStatusDTO updateOrderStatusDTO);

    public Page<OrderResponseDTO> getOrderByStatus(String status, int page, int size);

    public Page<OrderResponseDTO> getOrdersByDate(LocalDateTime startDate, LocalDateTime endDate, int page, int size);

    public Page<OrderResponseDTO> getOrderByEmail(String email, int page, int size);

    public UpdateOrderStatusResponseDTO cancelOrder(Long orderId);

    public OrderStatusSummaryDTO getOrderStatusSummary();
}
