package com.ecommerce.ecommercewebsite.services.superadmin;

import com.ecommerce.ecommercewebsite.dto.superadmin.OrderStatisticsDTO;
import com.ecommerce.ecommercewebsite.dto.superadmin.RecentOrderResponseDTO;
import com.ecommerce.ecommercewebsite.dto.superadmin.SuperAdminOrderDetailDTO;
import com.ecommerce.ecommercewebsite.dto.superadmin.SuperAdminOrderResponseDTO;
import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import com.ecommerce.ecommercewebsite.model.Order;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface SuperAdminOrderService {
    public Long countTotalOrders();

    public OrderStatisticsDTO getOrderStatistics();

    public List<RecentOrderResponseDTO> getRecentOrders();

    public Page<SuperAdminOrderResponseDTO> getAllOrders(int page, int size, String sort, OrderStatus status);

    public SuperAdminOrderDetailDTO getOrderDetails(Long orderId);

    public Order markOrderAsDelivered(Long orderId);
}
