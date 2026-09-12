package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.OrderItemResponseDTO;
import com.ecommerce.ecommercewebsite.dto.VendorOrderResponseDTO;
import com.ecommerce.ecommercewebsite.model.OrderItem;
import com.ecommerce.ecommercewebsite.model.OrderPayment;
import com.ecommerce.ecommercewebsite.model.VendorOrder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VendorOrderDetailsMapper {

    public VendorOrderResponseDTO mapToDTO(VendorOrder vendorOrder) {

        VendorOrderResponseDTO dto = new VendorOrderResponseDTO();

        // Order Information
        dto.setVendorOrderId(vendorOrder.getId());
        dto.setOrderId(vendorOrder.getOrder().getId());
        dto.setOrderNumber(vendorOrder.getOrder().getOrderNumber());

        dto.setStatus(vendorOrder.getStatus());
        dto.setCreatedAt(vendorOrder.getCreatedAt());

        // Customer Information
        dto.setCustomerName(vendorOrder.getOrder().getCustomer().getName());

        dto.setCustomerEmail(vendorOrder.getOrder().getCustomer().getEmail());

        dto.setPhoneNumber(vendorOrder.getOrder().getPhoneNumber());

        // Delivery Information
        if (vendorOrder.getOrder().getDistrict() != null) {
            dto.setDistrict(vendorOrder.getOrder().getDistrict().getDistrictName());
        }

        dto.setMunicipality(vendorOrder.getOrder().getMunicipality());

        dto.setStreetArea(vendorOrder.getOrder().getStreetArea());

        dto.setLandmark(vendorOrder.getOrder().getLandmark());

        // Payment Information
        OrderPayment payment = vendorOrder.getOrder().getPayment();

        if (payment != null) {
            dto.setPaymentAmount(payment.getAmount());
            dto.setPaymentMethod(payment.getPaymentMethod());
            dto.setPaymentStatus(payment.getStatus());
            dto.setTransactionUuid(payment.getTransactionUuid());
            dto.setPaidAt(payment.getPaidAt());
        }

        // Vendor Financial Information
        dto.setTotalAmount(vendorOrder.getTotalAmount());
        dto.setCommissionAmount(vendorOrder.getCommissionAmount());
        dto.setVendorEarning(vendorOrder.getVendorEarning());

        // Ordered Products
        List<OrderItemResponseDTO> items = new ArrayList<>();

        for (OrderItem orderItem : vendorOrder.getOrderItems()) {

            OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();

            itemDTO.setProductId(orderItem.getProduct().getProductId());

            itemDTO.setProductName(orderItem.getProductName());

            itemDTO.setSize(orderItem.getSize());

            itemDTO.setColor(orderItem.getColor());

            itemDTO.setQuantity(orderItem.getQuantity());

            itemDTO.setPriceAtPurchase(orderItem.getPriceAtPurchase());

            itemDTO.setTotalPrice(orderItem.getTotalPrice());

            items.add(itemDTO);
        }

        dto.setItems(items);

        return dto;
    }
}