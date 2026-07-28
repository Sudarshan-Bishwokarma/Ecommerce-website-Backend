package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.OrderItemResponseDTO;
import com.ecommerce.ecommercewebsite.dto.OrderResponseDTO;
import com.ecommerce.ecommercewebsite.dto.CustomerVendorOrderResponseDTO;
import com.ecommerce.ecommercewebsite.model.Order;
import com.ecommerce.ecommercewebsite.model.OrderItem;
import com.ecommerce.ecommercewebsite.model.VendorOrder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserOrderMapper {
    public OrderResponseDTO mapToDTO(Order order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setOrderId(order.getId());
        orderResponseDTO.setStatus(order.getStatus());
        orderResponseDTO.setTotalAmount(order.getTotalAmount());
        orderResponseDTO.setCreatedAt(order.getCreatedAt());

        List<CustomerVendorOrderResponseDTO> vendorOrderResponseDTOs = new ArrayList<>();
        for (VendorOrder vendorOrder : order.getVendorOrders()) {
            CustomerVendorOrderResponseDTO vendorOrderResponseDTO = new CustomerVendorOrderResponseDTO();
            vendorOrderResponseDTO.setVendorOrderId(vendorOrder.getId());
            vendorOrderResponseDTO.setVendorId(vendorOrder.getVendor().getId());
            vendorOrderResponseDTO.setVendorName(vendorOrder.getVendor().getName());
            vendorOrderResponseDTO.setTotalAmount(vendorOrder.getTotalAmount());

            List<OrderItemResponseDTO> orderItemResponseDTOs = new ArrayList<>();
            for (OrderItem orderItem : vendorOrder.getOrderItems()) {
                OrderItemResponseDTO orderItemResponseDTO = new OrderItemResponseDTO();
                orderItemResponseDTO.setProductId(orderItem.getProduct().getProductId());
                orderItemResponseDTO.setProductName(orderItem.getProduct().getProductName());
                orderItemResponseDTO.setQuantity(orderItem.getQuantity());
                orderItemResponseDTO.setPriceAtPurchase(orderItem.getPriceAtPurchase());
                orderItemResponseDTOs.add(orderItemResponseDTO);
            }

            vendorOrderResponseDTO.setItems(orderItemResponseDTOs);
            vendorOrderResponseDTOs.add(vendorOrderResponseDTO);
        }

        orderResponseDTO.setVendorOrders(vendorOrderResponseDTOs);

        return orderResponseDTO;
    }


}
