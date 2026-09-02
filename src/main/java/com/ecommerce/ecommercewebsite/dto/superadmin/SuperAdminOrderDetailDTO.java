package com.ecommerce.ecommercewebsite.dto.superadmin;

import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuperAdminOrderDetailDTO {
    // Order Information
    private Long orderId;
    private String orderNumber;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    // Customer Information
    private Long customerId;
    private String customerName;
    private String customerEmail;

    // Delivery Information
    private String fullName;
    private String phoneNumber;
    private String districtName;
    private String municipality;
    private String streetArea;
    private String landmark;
    // payment details
    private PaymentDetailDTO payment;
    // vendor details
    private List<VendorOrderDetailDTO> vendorOrders;


}
