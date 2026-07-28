package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.*;
import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface VendorOrderService {
    public Page<VendorOrderResponseDTO> getVendorOrders(String email, int page, int size);

    public VendorOrderResponseDTO getVendorOrderDetails(Long vendorOrderId, String email);

    public UpdateVendorOrderStatusResponseDTO updateVendorOrderStatus(String email, Long vendorOrderId, UpdateOrderStatusDTO updateOrderStatusDTO);

    public Page<VendorOrderResponseDTO> getOrderByStatus(String email, OrderStatus status, int page, int size);

    public Page<VendorOrderResponseDTO> getOrdersByDate(String email, LocalDateTime startDate, LocalDateTime endDate, int page, int size);

    public Page<VendorOrderResponseDTO> getOrderByEmail(String email, int page, int size);

    public UpdateVendorOrderStatusResponseDTO cancelOrder(String email, Long vendorOrderId);

    public VendorOrderStatusSummaryDTO getOrderStatusSummary(String email);

    public List<MonthlyOrderDTO> getMonthlyOrders();
}
