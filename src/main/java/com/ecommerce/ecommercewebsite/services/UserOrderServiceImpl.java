package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.OrderRequestDTO;
import com.ecommerce.ecommercewebsite.dto.OrderResponseDTO;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.*;
import com.ecommerce.ecommercewebsite.mappers.UserOrderMapper;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    UserOrderMapper orderMapper;

    @Override
    public OrderResponseDTO placeOrder(String email, OrderRequestDTO orderRequestDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ApiException(ProductErrorCode.CART_NOT_FOUND));
        if (cart.getItems().isEmpty()) {
            throw new ApiException(ProductErrorCode.CART_ITEM_NOT_FOUND);
        }
        Order order = new Order();
        order.setCustomer(user);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setShippingAddress(orderRequestDTO.getShippingAddress());
        order.setPaymentMethod(orderRequestDTO.getPaymentMethod());
        order.setNotes(orderRequestDTO.getNotes());
        Map<User, List<CartItem>> vendorCartItems = new HashMap<>();
        for (CartItem cartItem : cart.getItems()) {
            User getVendor = cartItem.getProduct().getVendor();
            vendorCartItems.computeIfAbsent(getVendor, k -> new ArrayList<>()).add(cartItem);

        }
        List<VendorOrder> vendorOrders = new ArrayList<>();
        BigDecimal orderTotalAmount = BigDecimal.ZERO;
        for (Map.Entry<User, List<CartItem>> entry : vendorCartItems.entrySet()) {

            User vendor = entry.getKey();

            List<CartItem> cartItems = entry.getValue();

            VendorOrder vendorOrder = new VendorOrder();

            vendorOrder.setOrder(order);
            vendorOrder.setVendor(vendor);
            vendorOrder.setStatus(OrderStatus.PENDING);
            List<OrderItem> orderItems = new ArrayList<>();
            BigDecimal price;
            BigDecimal vendorTotalAmount = BigDecimal.ZERO;
            for (CartItem cartItem : cartItems) {

                OrderItem orderItem = new OrderItem();

                orderItem.setVendorOrder(vendorOrder);

                orderItem.setProduct(cartItem.getProduct());

                orderItem.setQuantity(cartItem.getQuantity());

                if (cartItem.getProductVariant() != null) {
                    price = cartItem.getProductVariant().getPrice();
                } else {
                    price = cartItem.getProduct().getPrice();
                }
                orderItem.setPriceAtPurchase(price);
                BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                vendorTotalAmount = vendorTotalAmount.add(itemTotal);
                orderItems.add(orderItem);

            }
            vendorOrder.setOrderItems(orderItems);
            vendorOrder.setTotalAmount(vendorTotalAmount);
            BigDecimal commissionRate = new BigDecimal("0.10");
            BigDecimal commissionAmount = vendorTotalAmount.multiply(commissionRate);
            BigDecimal vendorEarning = vendorTotalAmount.subtract(commissionAmount);
            vendorOrder.setCommissionAmount(commissionAmount);
            vendorOrder.setVendorEarning(vendorEarning);
            vendorOrders.add(vendorOrder);
            orderTotalAmount = orderTotalAmount.add(vendorTotalAmount);

        }

        order.setTotalAmount(orderTotalAmount);
        order.setVendorOrders(vendorOrders);
        Order savedOrder = orderRepository.save(order);
        cart.getItems().clear();
        cartRepository.save(cart);
        OrderResponseDTO orderResponseDTO = orderMapper.mapToDTO(savedOrder);
        return orderResponseDTO;


    }

    @Override
    public Page<OrderResponseDTO> getUsersOrders(String email, int page, int size) {
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new UserNotFoundException("User not found"));
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findByCustomer(user, pageable);
        return orderPage.map(orderMapper::mapToDTO);
    }

    @Override
    public String cancelOrder(Long id, String email) {
        Order order = orderRepository.findById(id).
                orElseThrow(() -> new OrderNotFoundException("Order Not found"));
        if (!order.getCustomer().getEmail().equals(email)) {
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
        if (!order.getCustomer().getEmail().equals(email)) {
            throw new AccessDeniedException("Access Denied");
        }
        OrderResponseDTO responseDTO = orderMapper.mapToDTO(order);
        return responseDTO;
    }


}
