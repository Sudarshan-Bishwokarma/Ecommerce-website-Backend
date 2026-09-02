package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.superadmin.SuperAdminOrderResponseDTO;
import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import com.ecommerce.ecommercewebsite.model.Order;
import com.ecommerce.ecommercewebsite.model.VendorOrder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SuperAdminOrderMapper {
    public SuperAdminOrderResponseDTO MapToDTO(Order order) {
        SuperAdminOrderResponseDTO dto = new SuperAdminOrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setCustomerName(order.getCustomer().getName());
        dto.setCustomerEmail(order.getCustomer().getEmail());
        List<String> names = new ArrayList<>();
        for (VendorOrder vendorOrder : order.getVendorOrders()) {
            String vendorName = vendorOrder.getVendor().getName();
            names.add(vendorName);
        }
        dto.setVendorNames(names);
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;


    }
}
