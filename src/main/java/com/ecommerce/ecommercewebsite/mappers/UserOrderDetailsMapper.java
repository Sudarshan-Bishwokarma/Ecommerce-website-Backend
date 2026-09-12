package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.CustomerVendorOrderResponseDTO;
import com.ecommerce.ecommercewebsite.dto.OrderItemResponseDTO;
import com.ecommerce.ecommercewebsite.dto.users.OrderDetailsResponseDTO;
import com.ecommerce.ecommercewebsite.dto.users.OrderPaymentDetailsResponseDTO;
import com.ecommerce.ecommercewebsite.model.Order;
import com.ecommerce.ecommercewebsite.model.OrderItem;
import com.ecommerce.ecommercewebsite.model.OrderPayment;
import com.ecommerce.ecommercewebsite.model.VendorOrder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserOrderDetailsMapper {

    public OrderDetailsResponseDTO mapToDTO(Order order) {

        OrderDetailsResponseDTO responseDTO = new OrderDetailsResponseDTO();

        responseDTO.setOrderId(order.getId());
        responseDTO.setOrderNumber(order.getOrderNumber());
        responseDTO.setStatus(order.getStatus());
        responseDTO.setTotalAmount(order.getTotalAmount());
        responseDTO.setCreatedAt(order.getCreatedAt());
        responseDTO.setFullName(order.getFullName());
        responseDTO.setPhoneNumber(order.getPhoneNumber());
        responseDTO.setMunicipality(order.getMunicipality());
        responseDTO.setStreetArea(order.getStreetArea());
        responseDTO.setLandmark(order.getLandmark());

        if (order.getDistrict() != null) {
            responseDTO.setDistrict(order.getDistrict().getDistrictName());
        }

        List<CustomerVendorOrderResponseDTO> vendorOrders = new ArrayList<>();

        if (order.getVendorOrders() != null) {

            for (VendorOrder vendorOrder : order.getVendorOrders()) {

                CustomerVendorOrderResponseDTO vendorOrderDTO = new CustomerVendorOrderResponseDTO();

                vendorOrderDTO.setVendorOrderId(vendorOrder.getId());

                if (vendorOrder.getVendor() != null) {
                    vendorOrderDTO.setVendorId(vendorOrder.getVendor().getId());
                    vendorOrderDTO.setVendorName(vendorOrder.getVendor().getName());
                }

                vendorOrderDTO.setStatus(vendorOrder.getStatus());
                vendorOrderDTO.setTotalAmount(vendorOrder.getTotalAmount());

                List<OrderItemResponseDTO> items = new ArrayList<>();

                if (vendorOrder.getOrderItems() != null) {

                    for (OrderItem orderItem : vendorOrder.getOrderItems()) {

                        OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();

                        if (orderItem.getProduct() != null) {
                            itemDTO.setProductId(orderItem.getProduct().getProductId());
                        }

                        itemDTO.setProductName(orderItem.getProductName());
                        itemDTO.setSize(orderItem.getSize());
                        itemDTO.setColor(orderItem.getColor());
                        itemDTO.setQuantity(orderItem.getQuantity());
                        itemDTO.setPriceAtPurchase(orderItem.getPriceAtPurchase());
                        itemDTO.setTotalPrice(orderItem.getTotalPrice());

                        items.add(itemDTO);
                    }
                }

                vendorOrderDTO.setItems(items);
                vendorOrders.add(vendorOrderDTO);
            }
        }

        responseDTO.setVendorOrders(vendorOrders);

        OrderPayment payment = order.getPayment();

        if (payment != null) {

            OrderPaymentDetailsResponseDTO paymentDTO = new OrderPaymentDetailsResponseDTO();

            paymentDTO.setAmount(payment.getAmount());
            paymentDTO.setPaymentMethod(payment.getPaymentMethod());
            paymentDTO.setStatus(payment.getStatus());
            paymentDTO.setTransactionUuid(payment.getTransactionUuid());
            paymentDTO.setPaidAt(payment.getPaidAt());

            responseDTO.setPayment(paymentDTO);
        }

        return responseDTO;
    }
}