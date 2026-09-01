package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.enums.PaymentStatus;
import com.ecommerce.ecommercewebsite.model.FeaturedPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface FeaturedPaymentRepository extends JpaRepository<FeaturedPayment, Long> {
    Optional<FeaturedPayment> findByTransactionId(String transactionId);

    @Query("""
                SELECT COALESCE(SUM(f.amount), 0)
                FROM FeaturedPayment f
                WHERE f.status = :status
            """)
    BigDecimal getTotalAmountByStatus(PaymentStatus status);
}
