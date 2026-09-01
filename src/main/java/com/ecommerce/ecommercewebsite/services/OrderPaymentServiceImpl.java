package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.EmailDetailsDTO;
import com.ecommerce.ecommercewebsite.dto.PaymentResponseDTO;
import com.ecommerce.ecommercewebsite.enums.*;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.model.*;
import com.ecommerce.ecommercewebsite.repositories.CartRepository;
import com.ecommerce.ecommercewebsite.repositories.OrderPaymentRepository;
import com.ecommerce.ecommercewebsite.repositories.OrderRepository;
import com.ecommerce.ecommercewebsite.services.payment.EsewaPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderPaymentServiceImpl implements OrderPaymentService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private EsewaPaymentService esewaPaymentService;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private OrderPaymentRepository orderPaymentRepository;

    @Override
    public PaymentResponseDTO initiatePayment(Long orderId, PaymentMethod paymentMethod) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ApiException(OrderErrorCode.ORDER_NOT_FOUND));

        if (paymentMethod == PaymentMethod.CASH_ON_DELIVERY) {
            throw new ApiException(PaymentErrorCode.INVALID_PAYMENT_METHOD);
        }
        OrderPayment orderPayment = new OrderPayment();
        orderPayment.setOrder(order);
        orderPayment.setAmount(order.getTotalAmount());
        orderPayment.setPaymentMethod(paymentMethod);
        orderPayment.setStatus(PaymentStatus.PENDING);

        orderPaymentRepository.save(orderPayment);

        if (paymentMethod == PaymentMethod.ESEWA) {
            PaymentResponseDTO response = esewaPaymentService.createOrderPayment(order);

            orderPayment.setTransactionUuid(response.getTransactionId());
            orderPaymentRepository.save(orderPayment);
            return response;


        } else if (paymentMethod == PaymentMethod.KHALTI) {
            return null;
        } else {
            throw new ApiException(PaymentErrorCode.INVALID_PAYMENT_METHOD);
        }

    }

    @Transactional
    @Override
    public String handlePaymentSuccess(String transactionUuid) {
        OrderPayment orderPayment = orderPaymentRepository.findByTransactionUuid(transactionUuid)
                .orElseThrow(() -> new ApiException(PaymentErrorCode.PAYMENT_NOT_FOUND));

        Order order = orderPayment.getOrder();
        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new ApiException(PaymentErrorCode.PAYMENT_NOT_ALLOWED);
        }
        // reduce stock
        for (VendorOrder vendorOrder : order.getVendorOrders()) {

            for (OrderItem orderItem : vendorOrder.getOrderItems()) {

                ProductVariant variant = orderItem.getProductVariant();

                if (variant != null) {

                    if (variant.getStock() < orderItem.getQuantity()) {
                        throw new ApiException(ProductErrorCode.INSUFFICIENT_STOCK_AVAILABLE);
                    }

                    variant.setStock(
                            variant.getStock() - orderItem.getQuantity()
                    );

                } else {

                    Product product = orderItem.getProduct();
                    if (product.getStock() < orderItem.getQuantity()) {
                        throw new ApiException(ProductErrorCode.INSUFFICIENT_STOCK_AVAILABLE);
                    }

                    product.setStock(product.getStock() - orderItem.getQuantity());
                }
            }
        }

        BigDecimal commissionRate = new BigDecimal("0.10");

        for (VendorOrder vendorOrder : order.getVendorOrders()) {

            BigDecimal commissionAmount = vendorOrder.getTotalAmount().multiply(commissionRate);

            BigDecimal vendorEarning = vendorOrder.getTotalAmount().subtract(commissionAmount);
            vendorOrder.setCommissionAmount(commissionAmount);
            vendorOrder.setVendorEarning(vendorEarning);
            vendorOrder.setStatus(OrderStatus.PAID);

        }
        orderPayment.setStatus(PaymentStatus.SUCCESS);
        orderPayment.setPaidAt(LocalDateTime.now());
        orderPaymentRepository.save(orderPayment);
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
        //clear cart
        Cart cart = cartRepository.findByUser(order.getCustomer())
                .orElse(null);

        if (cart != null) {
            cart.getItems().clear();
            cartRepository.save(cart);
        }
        EmailDetailsDTO emailDetails = new EmailDetailsDTO();

        emailDetails.setRecipient(order.getCustomer().getEmail());

        emailDetails.setSubject(
                "Payment Successful - Order Confirmation"
        );


        emailDetails.setMsgBody(
                "Hello " + order.getCustomer().getName() + ",\n\n"
                        + "Your payment has been successfully completed.\n\n"
                        + "Order ID: " + orderPayment.getTransactionUuid() + "\n"
                        + "Status: PAID\n\n"
                        + "Your order is now being processed.\n\n"
                        + "Thank you for shopping with LocalConnect."
        );


        emailService.sendSimpleMail(emailDetails);
        return "Order payment completed successfully";
    }

    @Override
    @Transactional
    public String handlePaymentFailure(String transactionUuid) {

        OrderPayment orderPayment = orderPaymentRepository
                .findByTransactionUuid(transactionUuid)
                .orElseThrow(() -> new ApiException(PaymentErrorCode.PAYMENT_NOT_FOUND));

        Order order = orderPayment.getOrder();
        if (order.getStatus() == OrderStatus.PAID) {
            throw new ApiException(PaymentErrorCode.PAYMENT_ALREADY_COMPLETED);
        }

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new ApiException(
                    PaymentErrorCode.PAYMENT_NOT_ALLOWED
            );
        }

        order.setStatus(OrderStatus.PAYMENT_FAILED);

        orderRepository.save(order);


        return "Payment failed";
    }
}
