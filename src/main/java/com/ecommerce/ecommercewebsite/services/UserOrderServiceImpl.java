package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.OrderRequestDTO;
import com.ecommerce.ecommercewebsite.dto.OrderResponseDTO;
import com.ecommerce.ecommercewebsite.dto.PaymentResponseDTO;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import com.ecommerce.ecommercewebsite.enums.PaymentMethod;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.*;
import com.ecommerce.ecommercewebsite.mappers.UserOrderMapper;
import com.ecommerce.ecommercewebsite.model.*;
import com.ecommerce.ecommercewebsite.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private OrderPaymentService orderPaymentService;

    @Transactional
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
        if (orderRequestDTO.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY) {

            order.setStatus(OrderStatus.PROCESSING);

        } else {

            order.setStatus(OrderStatus.PENDING_PAYMENT);

        }
        order.setFullName(orderRequestDTO.getFullName());
        order.setPhoneNumber(orderRequestDTO.getPhoneNumber());
        District district = districtRepository.findById(orderRequestDTO.getDistrictId()).orElseThrow(() -> new ApiException(ProductErrorCode.DISTRICT_NOT_FOUND));
        order.setDistrict(district);
        order.setMunicipality(orderRequestDTO.getMunicipality());
        order.setStreetArea(orderRequestDTO.getStreetArea());
        order.setLandmark(orderRequestDTO.getLandmark());
        order.setCreatedAt(LocalDateTime.now());

        order.setNotes(orderRequestDTO.getNotes());
        Map<User, List<CartItem>> vendorCartItems = new HashMap<>();  // separate cart items based
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
            if (orderRequestDTO.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY) {
                vendorOrder.setStatus(OrderStatus.PROCESSING);
            } else {
                vendorOrder.setStatus(OrderStatus.PENDING_PAYMENT);
            }
            vendorOrder.setCreatedAt(LocalDateTime.now());
            List<OrderItem> orderItems = new ArrayList<>();
            BigDecimal price;
            BigDecimal vendorTotalAmount = BigDecimal.ZERO;
            for (CartItem cartItem : cartItems) {

                ProductVariant variant = cartItem.getProductVariant();
                if (variant != null) {
                    if (variant.getStock() < cartItem.getQuantity()) {
                        throw new ApiException(
                                ProductErrorCode.INSUFFICIENT_STOCK_AVAILABLE
                        );
                    }
                    if (orderRequestDTO.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY) {
                        variant.setStock(variant.getStock() - cartItem.getQuantity());
                    }
                } else {
                    Product product = cartItem.getProduct();

                    if (product.getStock() < cartItem.getQuantity()) {
                        throw new ApiException(ProductErrorCode.INSUFFICIENT_STOCK_AVAILABLE);
                    }

                    if (orderRequestDTO.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY) {
                        product.setStock(product.getStock() - cartItem.getQuantity());
                    }
                }
                OrderItem orderItem = new OrderItem();

                orderItem.setVendorOrder(vendorOrder);

                orderItem.setProduct(cartItem.getProduct());
                orderItem.setProductVariant(cartItem.getProductVariant());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setProductName(cartItem.getProduct().getProductName());
                if (cartItem.getProductVariant() != null) {
                    price = cartItem.getProductVariant().getPrice();
                    orderItem.setSize(cartItem.getProductVariant().getSize());
                    orderItem.setColor(cartItem.getProductVariant().getColor());
                } else {
                    price = cartItem.getProduct().getPrice();
                    orderItem.setSize(null);
                    orderItem.setColor(null);
                }
                orderItem.setPriceAtPurchase(price);
                BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                vendorTotalAmount = vendorTotalAmount.add(itemTotal);
                orderItems.add(orderItem);

            }
            vendorOrder.setOrderItems(orderItems);
            vendorOrder.setTotalAmount(vendorTotalAmount);
            vendorOrders.add(vendorOrder);
            orderTotalAmount = orderTotalAmount.add(vendorTotalAmount);

        }

        order.setTotalAmount(orderTotalAmount);
        order.setVendorOrders(vendorOrders);
        Order savedOrder = orderRepository.save(order);
        OrderResponseDTO orderResponseDTO = orderMapper.mapToDTO(savedOrder);
        if (orderRequestDTO.getPaymentMethod() == PaymentMethod.CASH_ON_DELIVERY) {


            cart.getItems().clear();
            cartRepository.save(cart);

            orderResponseDTO.setPayment(null);

        } else {

            String orderNumber = "ORD-" + savedOrder.getId();

            savedOrder.setOrderNumber(orderNumber);
            orderRepository.save(savedOrder);

            PaymentResponseDTO paymentResponse = orderPaymentService.initiatePayment(savedOrder.getId(), orderRequestDTO.getPaymentMethod());

            orderResponseDTO.setPayment(paymentResponse);
        }

        return orderResponseDTO;

    }

    @Override
    public Page<OrderResponseDTO> getUsersOrders(String email, String sort, OrderStatus status, int page, int size) {
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new UserNotFoundException("User not found"));
        Pageable pageable;
        if (sort != null) {
            switch (sort) {
                case "oldest":
                    pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
                    break;
                case "newest":
                    pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
                    break;
                case "low":
                    pageable = PageRequest.of(page, size, Sort.by("totalAmount").ascending());
                    break;
                case "high":
                    pageable = PageRequest.of(page, size, Sort.by("totalAmount").descending());
                    break;
                default:
                    pageable = PageRequest.of(page, size);
            }
        } else {
            pageable = PageRequest.of(page, size);
        }
        Page<Order> allOrders;
        if (status != null) {
            allOrders = orderRepository.findByCustomerAndStatus(user, status, pageable);
        } else {
            allOrders = orderRepository.findByCustomer(user, pageable);
        }


        return allOrders.map(orderMapper::mapToDTO);
    }

    @Override
    public String cancelOrder(Long id, String email) {
        Order order = orderRepository.findById(id).
                orElseThrow(() -> new OrderNotFoundException("Order Not found"));
        if (!order.getCustomer().getEmail().equals(email)) {
            throw new AccessDeniedException("Access Denied");
        }
        order.setStatus(OrderStatus.CANCELLED);
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
