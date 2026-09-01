package com.ecommerce.ecommercewebsite.services.superadmin;

import com.ecommerce.ecommercewebsite.dto.superadmin.OrderStatisticsDTO;
import com.ecommerce.ecommercewebsite.dto.superadmin.RecentOrderResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public interface SuperAdminOrderService {
    public Long countTotalOrders();

    public OrderStatisticsDTO getOrderStatistics();

    public List<RecentOrderResponseDTO> getRecentOrders();

    
}
