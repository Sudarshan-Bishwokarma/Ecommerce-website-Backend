package com.ecommerce.ecommercewebsite.dto.users;

import com.ecommerce.ecommercewebsite.dto.CustomerVendorOrderResponseDTO;
import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsResponseDTO {
    private Long orderId;
    private String orderNumber;

    private String fullName;
    private String phoneNumber;
    private String district;
    private String municipality;
    private String streetArea;
    private String landmark;

    private OrderStatus status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;

    private List<CustomerVendorOrderResponseDTO> vendorOrders;

    private OrderPaymentDetailsResponseDTO payment;

}
