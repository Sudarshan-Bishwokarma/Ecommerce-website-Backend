package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.*;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
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
import org.springframework.stereotype.Service;

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

    @Override
    public Page<VendorOrderResponseDTO> getVendorOrders(String email, int page, int size) {
        User vendor = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.VENDOR_NOT_FOUND));
        Pageable pageable = PageRequest.of(page, size);
        Page<VendorOrder> myOrders = vendorOrderRepository.findByVendor(vendor, pageable);
        return myOrders.map(vendorOrderMapper::mapToDTO);
    }

    @Override
    public VendorOrderResponseDTO getVendorOrderDetails(Long vendorOrderId, String email) {
        User vendor = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        VendorOrder vendorOrder = vendorOrderRepository.findByIdAndVendor(vendorOrderId, vendor).orElseThrow(() -> new ApiException(ProductErrorCode.VENDOR_ORDER_NOT_FOUND));
        VendorOrderResponseDTO responseDTO = vendorOrderMapper.mapToDTO(vendorOrder);
        return responseDTO;
    }

    @Override
    public UpdateVendorOrderStatusResponseDTO updateVendorOrderStatus(String email, Long vendorOrderId, UpdateOrderStatusDTO updateOrderStatusDTO) {
        User vendor = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.VENDOR_NOT_FOUND));
        VendorOrder vendorOrder = vendorOrderRepository.findByIdAndVendor(vendorOrderId, vendor).orElseThrow(() -> new ApiException(ProductErrorCode.VENDOR_ORDER_NOT_FOUND));
        vendorOrder.setStatus(updateOrderStatusDTO.getOrderStatus());
        VendorOrder savedVendorOrder = vendorOrderRepository.save(vendorOrder);
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
        return vendorOrders.map(vendorOrderMapper::mapToDTO);
    }

    @Override
    public Page<VendorOrderResponseDTO> getOrdersByDate(String email, LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        User vendor = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.VENDOR_NOT_FOUND));
        Pageable pageable = PageRequest.of(page, size);
        Page<VendorOrder> vendorOrders = vendorOrderRepository.findByVendorAndOrder_CreatedAtBetween(vendor, startDate, endDate, pageable);
        return vendorOrders.map(vendorOrderMapper::mapToDTO);
    }

    @Override
    public Page<VendorOrderResponseDTO> getOrderByEmail(String email, int page, int size) {
        User vendor = userRepository.findByEmail(email).
                orElseThrow(() -> new ApiException(AuthErrorCode.VENDOR_NOT_FOUND));
        Pageable pageable = PageRequest.of(page, size);
        Page<VendorOrder> vendorOrders = vendorOrderRepository.findByVendor_Email(email, pageable);
        return vendorOrders.map(vendorOrderMapper::mapToDTO);

    }

    @Override
    public UpdateVendorOrderStatusResponseDTO cancelOrder(String email, Long vendorOrderId) {
        User vendor = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.VENDOR_NOT_FOUND));
        VendorOrder vendorOrder = vendorOrderRepository.findByIdAndVendor(vendorOrderId, vendor).orElseThrow(() -> new ApiException(ProductErrorCode.VENDOR_ORDER_NOT_FOUND));
        vendorOrder.setStatus(OrderStatus.CANCELED);
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
        Long pending = vendorOrderRepository.countByVendorAndStatus(vendor, OrderStatus.PENDING);
        Long cancelled = vendorOrderRepository.countByVendorAndStatus(vendor, OrderStatus.CANCELED);
        Long delivered = vendorOrderRepository.countByVendorAndStatus(vendor, OrderStatus.DELIVERED);
        Long paid = vendorOrderRepository.countByVendorAndStatus(vendor, OrderStatus.PAID);
        Long shipped = vendorOrderRepository.countByVendorAndStatus(vendor, OrderStatus.SHIPPED);
        VendorOrderStatusSummaryDTO responseDTO = new VendorOrderStatusSummaryDTO();
        responseDTO.setPending(pending);
        responseDTO.setCancelled(cancelled);
        responseDTO.setDelivered(delivered);
        responseDTO.setPaid(paid);
        responseDTO.setShipped(shipped);
        return responseDTO;
    }

    @Override
    public List<MonthlyOrderDTO> getMonthlyOrders() {

        return orderRepository.getMonthlyOrders();
    }

}
