package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.OrderItemResponseDTO;
import com.ecommerce.ecommercewebsite.dto.VendorOrderResponseDTO;
import com.ecommerce.ecommercewebsite.model.OrderItem;
import com.ecommerce.ecommercewebsite.model.VendorOrder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VendorOrderMapper {
    public VendorOrderResponseDTO mapToDTO(VendorOrder vendorOrder) {
        VendorOrderResponseDTO vendorOrderResponseDTO = new VendorOrderResponseDTO();
        vendorOrderResponseDTO.setVendorOrderId(vendorOrder.getId());
        vendorOrderResponseDTO.setOrderId(vendorOrder.getOrder().getId());
        vendorOrderResponseDTO.setCustomerName(vendorOrder.getOrder().getCustomer().getName());
        vendorOrderResponseDTO.setCustomerEmail(vendorOrder.getOrder().getCustomer().getEmail());
        vendorOrderResponseDTO.setShippingAddress(vendorOrder.getOrder().getShippingAddress());
        vendorOrderResponseDTO.setStatus(vendorOrder.getStatus());
        vendorOrderResponseDTO.setCreatedAt(vendorOrder.getOrder().getCreatedAt());
        vendorOrderResponseDTO.setTotalAmount(vendorOrder.getTotalAmount());
        vendorOrderResponseDTO.setCommissionAmount(vendorOrder.getCommissionAmount());
        vendorOrderResponseDTO.setVendorEarning(vendorOrder.getVendorEarning());
        List<OrderItemResponseDTO> orderItems = new ArrayList<>();
        for (OrderItem orderItem : vendorOrder.getOrderItems()) {
            OrderItemResponseDTO orderItemResponseDTO = new OrderItemResponseDTO();
            orderItemResponseDTO.setProductId(orderItem.getProduct().getProductId());
            orderItemResponseDTO.setProductName(orderItem.getProduct().getProductName());
            orderItemResponseDTO.setQuantity(orderItem.getQuantity());
            orderItemResponseDTO.setPriceAtPurchase(orderItem.getPriceAtPurchase());
            orderItems.add(orderItemResponseDTO);
        }
        vendorOrderResponseDTO.setItems(orderItems);
        return vendorOrderResponseDTO;

    }
}
