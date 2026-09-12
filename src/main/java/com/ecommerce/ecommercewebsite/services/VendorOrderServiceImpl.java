package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.*;
import com.ecommerce.ecommercewebsite.dto.vendor.VendorOrderListResponseDTO;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.enums.OrderErrorCode;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.mappers.VendorOrderDetailsMapper;
import com.ecommerce.ecommercewebsite.mappers.VendorOrderMapper;
import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.model.VendorOrder;
import com.ecommerce.ecommercewebsite.repositories.OrderRepository;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import com.ecommerce.ecommercewebsite.repositories.VendorOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VendorOrderServiceImpl implements VendorOrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    VendorOrderRepository vendorOrderRepository;
    @Autowired
    VendorOrderMapper vendorOrderMapper;
    @Autowired
    private VendorOrderDetailsMapper vendorOrderDetailsMapper;
    @Autowired
    private EmailService emailService;

    @Override
    public Page<VendorOrderListResponseDTO> getVendorOrders(String email, int page, int size, OrderStatus status, String sort) {
        User vendor = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.VENDOR_NOT_FOUND));
        Pageable pageable;

        switch (sort) {
            case "oldest":
                pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
                break;
            case "high":
                pageable = PageRequest.of(page, size, Sort.by("totalAmount").descending());
                break;
            case "low":
                pageable = PageRequest.of(page, size, Sort.by("totalAmount").ascending());
                break;
            case "newest":
                pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

            default:
                pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
                break;
        }
        Page<VendorOrder> vendorOrders;

        if (status != null) {
            vendorOrders = vendorOrderRepository.findByVendorAndStatus(vendor, status, pageable);
        } else {
            vendorOrders = vendorOrderRepository.findByVendor(vendor, pageable);
        }

        return vendorOrders.map(vendorOrderMapper::mapToDTO);


    }

    @Override
    public VendorOrderResponseDTO getVendorOrderDetails(Long vendorOrderId, String email) {
        User vendor = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        VendorOrder vendorOrder = vendorOrderRepository.findByIdAndVendor(vendorOrderId, vendor).orElseThrow(() -> new ApiException(ProductErrorCode.VENDOR_ORDER_NOT_FOUND));
        VendorOrderResponseDTO responseDTO = vendorOrderDetailsMapper.mapToDTO(vendorOrder);
        return responseDTO;
    }

    @Override
    public UpdateVendorOrderStatusResponseDTO updateVendorOrderStatus(String email, Long vendorOrderId, UpdateOrderStatusDTO updateOrderStatusDTO) {
        User vendor = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.VENDOR_NOT_FOUND));
        VendorOrder vendorOrder = vendorOrderRepository.findByIdAndVendor(vendorOrderId, vendor).orElseThrow(() -> new ApiException(ProductErrorCode.VENDOR_ORDER_NOT_FOUND));
        // Only allow PAID or PROCESSING  are DELIVERED
        if (updateOrderStatusDTO.getOrderStatus() != OrderStatus.DELIVERED) {
            throw new ApiException(OrderErrorCode.INVALID_ORDER_STATUS);
        }
        if (vendorOrder.getStatus() != OrderStatus.PAID &&
                vendorOrder.getStatus() != OrderStatus.PROCESSING) {

            throw new ApiException(OrderErrorCode.INVALID_ORDER_STATUS);
        }
        if (vendorOrder.getStatus() == OrderStatus.PROCESSING && vendorOrder.getCommissionAmount() == null) {

            BigDecimal commissionRate = new BigDecimal("0.10");

            BigDecimal commissionAmount = vendorOrder.getTotalAmount().multiply(commissionRate);

            BigDecimal vendorEarning = vendorOrder.getTotalAmount().subtract(commissionAmount);

            vendorOrder.setCommissionAmount(commissionAmount);
            vendorOrder.setVendorEarning(vendorEarning);

        }
        vendorOrder.setDeliveredAt(LocalDateTime.now());

        // Change status to DELIVERED
        vendorOrder.setStatus(OrderStatus.DELIVERED);

        VendorOrder savedVendorOrder = vendorOrderRepository.save(vendorOrder);

        // Get customer
        User customer = savedVendorOrder.getOrder().getCustomer();

        // Prepare email
        EmailDetailsDTO emailDetails = new EmailDetailsDTO();

        emailDetails.setRecipient(customer.getEmail());

        emailDetails.setSubject("Your Order Has Been Delivered");

        emailDetails.setMsgBody(
                "Dear " + customer.getName() + ",\n\n" +
                        "Your order has been successfully marked as delivered.\n\n" +
                        "Vendor Order ID: " + savedVendorOrder.getId() + "\n" +
                        "Status: DELIVERED\n\n" +
                        "Thank you for shopping with LocalConnect.\n\n" +
                        "Best regards,\n" +
                        "LocalConnect Team"
        );

        // Send email
        emailService.sendSimpleMail(emailDetails);

        UpdateVendorOrderStatusResponseDTO responseDTO = new UpdateVendorOrderStatusResponseDTO();
        responseDTO.setVendorOrderId(savedVendorOrder.getId());
        responseDTO.setStatus(savedVendorOrder.getStatus());
        responseDTO.setUpdatedAt(LocalDateTime.now());
        return responseDTO;


    }

    @Override
    public Page<VendorOrderResponseDTO> getOrderByStatus(String email, OrderStatus status, int page, int size) {
        User vendor = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        Pageable pageable = PageRequest.of(page, size);
        Page<VendorOrder> vendorOrders = vendorOrderRepository.findByVendorAndStatus(vendor, status, pageable);
        return vendorOrders.map(vendorOrderDetailsMapper::mapToDTO);
    }

    @Override
    public Page<VendorOrderResponseDTO> getOrdersByDate(String email, LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        User vendor = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.VENDOR_NOT_FOUND));
        Pageable pageable = PageRequest.of(page, size);
        Page<VendorOrder> vendorOrders = vendorOrderRepository.findByVendorAndOrder_CreatedAtBetween(vendor, startDate, endDate, pageable);
        return vendorOrders.map(vendorOrderDetailsMapper::mapToDTO);
    }

    @Override
    public Page<VendorOrderResponseDTO> getOrderByEmail(String email, int page, int size) {
        User vendor = userRepository.findByEmail(email).
                orElseThrow(() -> new ApiException(AuthErrorCode.VENDOR_NOT_FOUND));
        Pageable pageable = PageRequest.of(page, size);
        Page<VendorOrder> vendorOrders = vendorOrderRepository.findByVendor_Email(email, pageable);
        return vendorOrders.map(vendorOrderDetailsMapper::mapToDTO);

    }

    @Override
    public UpdateVendorOrderStatusResponseDTO cancelOrder(String email, Long vendorOrderId) {
        User vendor = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.VENDOR_NOT_FOUND));
        VendorOrder vendorOrder = vendorOrderRepository.findByIdAndVendor(vendorOrderId, vendor).orElseThrow(() -> new ApiException(ProductErrorCode.VENDOR_ORDER_NOT_FOUND));
        vendorOrder.setStatus(OrderStatus.CANCELLED);
        VendorOrder savedVendorOrder = vendorOrderRepository.save(vendorOrder);
        UpdateVendorOrderStatusResponseDTO responseDTO = new UpdateVendorOrderStatusResponseDTO();
        responseDTO.setVendorOrderId(savedVendorOrder.getId());
        responseDTO.setStatus(savedVendorOrder.getStatus());
        responseDTO.setUpdatedAt(LocalDateTime.now());
        return responseDTO;
    }

    @Override
    public VendorOrderStatusSummaryDTO getOrderStatusSummary(String email) {
        User vendor = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        Long pending = vendorOrderRepository.countByVendorAndStatus(vendor, OrderStatus.PENDING_PAYMENT);
        Long cancelled = vendorOrderRepository.countByVendorAndStatus(vendor, OrderStatus.CANCELLED);
        Long delivered = vendorOrderRepository.countByVendorAndStatus(vendor, OrderStatus.DELIVERED);
        Long paid = vendorOrderRepository.countByVendorAndStatus(vendor, OrderStatus.PAID);
        Long shipped = vendorOrderRepository.countByVendorAndStatus(vendor, OrderStatus.SHIPPED);
        Long processing = vendorOrderRepository.countByVendorAndStatus(vendor, OrderStatus.PROCESSING);
        VendorOrderStatusSummaryDTO responseDTO = new VendorOrderStatusSummaryDTO();
        responseDTO.setPending(pending);
        responseDTO.setCancelled(cancelled);
        responseDTO.setDelivered(delivered);
        responseDTO.setPaid(paid);
        responseDTO.setShipped(shipped);
        responseDTO.setProcessing(processing);
        return responseDTO;
    }

    @Override
    public List<MonthlyOrderDTO> getMonthlyOrders() {

        return orderRepository.getMonthlyOrders();
    }

}
