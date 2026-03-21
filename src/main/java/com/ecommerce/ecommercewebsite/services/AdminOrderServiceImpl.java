package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.*;
import com.ecommerce.ecommercewebsite.exception.OrderNotFoundException;
import com.ecommerce.ecommercewebsite.model.Order;
import com.ecommerce.ecommercewebsite.model.OrderItem;
import com.ecommerce.ecommercewebsite.model.OrderStatus;
import com.ecommerce.ecommercewebsite.repositories.OrderRepository;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminOrderServiceImpl implements AdminOrderService {
    @Autowired
    OrderRepository orderRepository;

    @Override
    public Page<OrderResponseDTO> getAllUsersOrders(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(this::mapToDTO);
    }

    @Override
    public OrderDetailDTO getMoreOrderDetails(Long orderId) {
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new OrderNotFoundException("Order not found"));
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setOrderId(order.getId());
        orderDetailDTO.setUserEmail(order.getUser().getEmail());
        orderDetailDTO.setShippingAddress(order.getShippingAddress());
        orderDetailDTO.setPaymentMethod(order.getPaymentMethod());
        orderDetailDTO.setOrderStatus(order.getStatus());
        orderDetailDTO.setOrderDateTime(order.getCreatedAt());
        orderDetailDTO.setTotalPrice(order.getTotalAmount());
        List<OrderItemDTO> orderItems = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            orderItemDTO.setProductName(orderItem.getProduct().getProductName());
            orderItemDTO.setQuantity(orderItem.getQuantity());
            double subTotal = orderItem.getPriceAtPurchase() * orderItem.getQuantity();
            orderItemDTO.setSubTotal(subTotal);
            orderItems.add(orderItemDTO);
        }
        orderDetailDTO.setOrderItems(orderItems);

        return orderDetailDTO;
    }

    @Override
    public UpdateOrderStatusResponseDTO updateOrderStatus(Long orderId, UpdateOrderStatusDTO updateOrderStatusDTO) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setStatus(updateOrderStatusDTO.getOrderStatus());
        orderRepository.save(order);
        UpdateOrderStatusResponseDTO dto = new UpdateOrderStatusResponseDTO();
        dto.setOrderId(order.getId());
        dto.setOrderStatus(order.getStatus());
        dto.setMessage("Order Updated");
        dto.setUpdateTime(LocalDateTime.now());
        return dto;

    }

    @Override
    public Page<OrderResponseDTO> getOrderByStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        //   conversion  of  string to enum   value
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        Page<Order> orderPage = orderRepository.findByStatus(orderStatus, pageable);
        return orderPage.map(this::mapToDTO);

    }

    @Override
    public Page<OrderResponseDTO> getOrdersByDate(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> pageOrder = orderRepository.findByCreatedAtBetween(startDate, endDate, pageable);
        return pageOrder.map(this::mapToDTO);
    }

    //helper  class
    public OrderResponseDTO mapToDTO(Order order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setOrderId(order.getId());
        orderResponseDTO.setOrderStatus(order.getStatus().name());
        orderResponseDTO.setTotalPrice(order.getTotalAmount());
        return orderResponseDTO;
    }

}



/*
Hibernate creates entity objects → returns them to Spring Data JPA → Spring Data JPA wraps them
(e.g., in Page/Optional) → returns the final object to your code.
 */