package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.PaymentResponseDTO;
import com.ecommerce.ecommercewebsite.dto.superadmin.OrderItemDetailDTO;
import com.ecommerce.ecommercewebsite.dto.superadmin.PaymentDetailDTO;
import com.ecommerce.ecommercewebsite.dto.superadmin.SuperAdminOrderDetailDTO;
import com.ecommerce.ecommercewebsite.dto.superadmin.VendorOrderDetailDTO;
import com.ecommerce.ecommercewebsite.model.Order;
import com.ecommerce.ecommercewebsite.model.OrderItem;
import com.ecommerce.ecommercewebsite.model.OrderPayment;
import com.ecommerce.ecommercewebsite.model.VendorOrder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SuperAdminOrderDetailMapper {
    public SuperAdminOrderDetailDTO mapToDTO(Order order) {
        SuperAdminOrderDetailDTO dto = new SuperAdminOrderDetailDTO();
        // Order Information
        dto.setOrderId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setCreatedAt(order.getCreatedAt());
        // Customer Information
        dto.setCustomerId(order.getCustomer().getId());
        dto.setCustomerName(order.getCustomer().getName());
        dto.setCustomerEmail(order.getCustomer().getEmail());

        // Delivery Information
        dto.setFullName(order.getFullName());
        dto.setPhoneNumber(order.getPhoneNumber());
        dto.setDistrictName(order.getDistrict().getDistrictName());
        dto.setMunicipality(order.getMunicipality());
        dto.setStreetArea(order.getStreetArea());
        dto.setLandmark(order.getLandmark());
        // payment details
        OrderPayment payment = order.getPayment();
        if (payment != null) {
            PaymentDetailDTO paymentDetailDTO = new PaymentDetailDTO();
            paymentDetailDTO.setOrderPaymentId(payment.getOrderPaymentId());
            paymentDetailDTO.setAmount(payment.getAmount());
            paymentDetailDTO.setPaymentMethod(payment.getPaymentMethod());
            paymentDetailDTO.setTransactionUuid(payment.getTransactionUuid());
            paymentDetailDTO.setPaidAt(payment.getPaidAt());
            paymentDetailDTO.setStatus(payment.getStatus());
            dto.setPayment(paymentDetailDTO);
        }

        //  vendor order details
        List<VendorOrderDetailDTO> vendorDetails = new ArrayList<>();
        for (VendorOrder vendorOrder : order.getVendorOrders()) {
            VendorOrderDetailDTO vendorOrderDetailDTO = new VendorOrderDetailDTO();
            vendorOrderDetailDTO.setVendorOrderId(vendorOrder.getId());
            vendorOrderDetailDTO.setVendorId(vendorOrder.getVendor().getId());
            vendorOrderDetailDTO.setVendorName(vendorOrder.getVendor().getName());
            vendorOrderDetailDTO.setStatus(vendorOrder.getStatus());
            vendorOrderDetailDTO.setCreatedAt(vendorOrder.getCreatedAt());
            vendorOrderDetailDTO.setTotalAmount(vendorOrder.getTotalAmount());
            vendorOrderDetailDTO.setCommissionAmount(vendorOrder.getCommissionAmount());
            vendorOrderDetailDTO.setVendorEarning(vendorOrder.getVendorEarning());
            List<OrderItemDetailDTO> orderItems = new ArrayList<>();
            for (OrderItem orderItem : vendorOrder.getOrderItems()) {
                OrderItemDetailDTO orderItemDetailDTO = new OrderItemDetailDTO();
                orderItemDetailDTO.setOrderItemId(orderItem.getId());
                orderItemDetailDTO.setProductId(orderItem.getProduct().getProductId());
                orderItemDetailDTO.setProductName(orderItem.getProduct().getProductName());
                orderItemDetailDTO.setQuantity(orderItem.getQuantity());
                orderItemDetailDTO.setPriceAtPurchase(orderItem.getPriceAtPurchase());
                orderItemDetailDTO.setTotalPrice(orderItem.getTotalPrice());
                if (orderItem.getProductVariant() != null) {
                    orderItemDetailDTO.setSize(orderItem.getProductVariant().getSize());
                    orderItemDetailDTO.setColor(orderItem.getProductVariant().getColor());
                } else {
                    orderItemDetailDTO.setColor(null);
                    orderItemDetailDTO.setSize(null);
                }
                orderItems.add(orderItemDetailDTO);
            }
            vendorOrderDetailDTO.setItems(orderItems);
            vendorDetails.add(vendorOrderDetailDTO);

        }
        dto.setVendorOrders(vendorDetails);
        return dto;
    }
}
