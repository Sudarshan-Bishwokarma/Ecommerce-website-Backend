package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.OrderRequestDTO;
import com.ecommerce.ecommercewebsite.dto.OrderResponseDTO;
import com.ecommerce.ecommercewebsite.exception.CartItemNotFoundException;
import com.ecommerce.ecommercewebsite.exception.CartNotFoundException;
import com.ecommerce.ecommercewebsite.exception.OrderNotFoundException;
import com.ecommerce.ecommercewebsite.exception.UserNotFoundException;
import com.ecommerce.ecommercewebsite.model.*;
import com.ecommerce.ecommercewebsite.repositories.CartRepository;
import com.ecommerce.ecommercewebsite.repositories.OrderRepository;
import com.ecommerce.ecommercewebsite.repositories.ProductRepository;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserOrderServiceImpl implements UserOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    CartRepository cartRepository;

    @Override
    public OrderResponseDTO placeOrder(String email, OrderRequestDTO orderRequestDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        if (cart.getItems().isEmpty()) {
            throw new CartItemNotFoundException("Cart Items  not found");
        }
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setShippingAddress(orderRequestDTO.getShippingAddress());
        order.setPaymentMethod(orderRequestDTO.getPaymentMethod());
        order.setNotes(orderRequestDTO.getNotes());
        double total_Amount = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getProduct().getProductPrice());
            double subTotal = cartItem.getProduct().getProductPrice() * cartItem.getQuantity();
            total_Amount += subTotal;
            orderItems.add(orderItem);
        }
        order.setTotalAmount(total_Amount);
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        // clear  cart after order   placed
        cart.getItems().clear();
        cartRepository.save(cart);
        OrderResponseDTO responseDTO = mapDTO(savedOrder);
        return responseDTO;
    }

    @Override
    public Page<OrderResponseDTO> getUsersOrders(String email, int page, int size) {
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new UserNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findByUser(user, pageable);
        return orderPage.map(this::mapDTO);
    }

    @Override
    public String cancelOrder(Long id, String email) {
        Order order = orderRepository.findById(id).
                orElseThrow(() -> new OrderNotFoundException("Order Not found"));
        if (!order.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("Access Denied");
        }
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
        return " Order has been cancelled";
    }

    @Override
    public OrderResponseDTO getOrderById(Long orderId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order Not found"));
        if (!order.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("Access Denied");
        }
        OrderResponseDTO responseDTO = mapDTO(order);
        return responseDTO;
    }

    //  helper class
    public OrderResponseDTO mapDTO(Order savedOrder) {
        OrderResponseDTO responseDTO = new OrderResponseDTO();
        responseDTO.setOrderId(savedOrder.getId());
        responseDTO.setTotalPrice(savedOrder.getTotalAmount());
        responseDTO.setOrderStatus(savedOrder.getStatus().name());
        return responseDTO;
    }
}
