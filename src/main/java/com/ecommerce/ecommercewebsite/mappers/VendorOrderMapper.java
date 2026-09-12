package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.OrderItemResponseDTO;
import com.ecommerce.ecommercewebsite.dto.VendorOrderResponseDTO;
import com.ecommerce.ecommercewebsite.dto.vendor.VendorOrderListResponseDTO;
import com.ecommerce.ecommercewebsite.model.OrderItem;
import com.ecommerce.ecommercewebsite.model.VendorOrder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VendorOrderMapper {
    public VendorOrderListResponseDTO mapToDTO(VendorOrder vendorOrder) {

        VendorOrderListResponseDTO dto = new VendorOrderListResponseDTO();

        dto.setVendorOrderId(vendorOrder.getId());
        dto.setOrderId(vendorOrder.getOrder().getId());
        dto.setOrderNumber(vendorOrder.getOrder().getOrderNumber());
        // Customer
        dto.setCustomerName(vendorOrder.getOrder().getCustomer().getName());

        // Order
        dto.setStatus(vendorOrder.getStatus());
        dto.setCreatedAt(vendorOrder.getCreatedAt());

        // Order Summary
        List<OrderItem> orderItems = vendorOrder.getOrderItems();

        dto.setTotalItems(orderItems.size());

        int totalUnits = 0;

        for (OrderItem item : orderItems) {
            totalUnits += item.getQuantity();
        }

        dto.setTotalUnits(totalUnits);

        // Vendor Order Amount
        dto.setTotalAmount(vendorOrder.getTotalAmount());

        return dto;
    }
}
