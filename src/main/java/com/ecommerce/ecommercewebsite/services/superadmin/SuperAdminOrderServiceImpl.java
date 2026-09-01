package com.ecommerce.ecommercewebsite.services.superadmin;

import com.ecommerce.ecommercewebsite.dto.superadmin.OrderStatisticsDTO;
import com.ecommerce.ecommercewebsite.dto.superadmin.RecentOrderResponseDTO;
import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import com.ecommerce.ecommercewebsite.model.Order;
import com.ecommerce.ecommercewebsite.model.VendorOrder;
import com.ecommerce.ecommercewebsite.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SuperAdminOrderServiceImpl implements SuperAdminOrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Long countTotalOrders() {
        return orderRepository.count();

    }

    @Override
    public OrderStatisticsDTO getOrderStatistics() {
        Long pendingPayment = orderRepository.countByStatus(OrderStatus.PENDING_PAYMENT);

        Long paid = orderRepository.countByStatus(OrderStatus.PAID);

        Long processing = orderRepository.countByStatus(OrderStatus.PROCESSING);

        Long shipped = orderRepository.countByStatus(OrderStatus.SHIPPED);

        Long delivered = orderRepository.countByStatus(OrderStatus.DELIVERED);

        Long cancelled = orderRepository.countByStatus(OrderStatus.CANCELLED);

        Long paymentFailed = orderRepository.countByStatus(OrderStatus.PAYMENT_FAILED);
        return new OrderStatisticsDTO(
                pendingPayment,
                paid,
                processing,
                shipped,
                delivered,
                cancelled,
                paymentFailed
        );

    }

    @Transactional(readOnly = true)
    @Override
    public List<RecentOrderResponseDTO> getRecentOrders() {
        List<Order> orders = orderRepository.findTop5ByOrderByCreatedAtDesc();
        List<RecentOrderResponseDTO> recentOrders = new ArrayList<>();
        for (Order order : orders) {
            RecentOrderResponseDTO recentOrderResponseDTO = new RecentOrderResponseDTO();
            recentOrderResponseDTO.setOrderId(order.getId());
            recentOrderResponseDTO.setOrderNumber(order.getOrderNumber());
            recentOrderResponseDTO.setCustomerName(order.getCustomer().getName());
            List<String> vendorNames = new ArrayList<>();

            for (VendorOrder vendorOrder : order.getVendorOrders()) {

                String vendorName = vendorOrder.getVendor().getName();

                vendorNames.add(vendorName);
            }
            recentOrderResponseDTO.setVendorNames(vendorNames);
            recentOrderResponseDTO.setTotalAmount(order.getTotalAmount());
            recentOrderResponseDTO.setStatus(order.getStatus());
            recentOrders.add(recentOrderResponseDTO);
        }
        return recentOrders;
    }

    
}
