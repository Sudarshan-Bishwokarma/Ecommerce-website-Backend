package com.ecommerce.ecommercewebsite.dto.users;

import com.ecommerce.ecommercewebsite.enums.PaymentMethod;
import com.ecommerce.ecommercewebsite.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentDetailsResponseDTO {
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String transactionUuid;
    private LocalDateTime paidAt;
}
