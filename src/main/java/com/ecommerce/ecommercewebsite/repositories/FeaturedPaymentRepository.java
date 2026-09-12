package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.enums.PaymentStatus;
import com.ecommerce.ecommercewebsite.model.FeaturedPayment;
import com.ecommerce.ecommercewebsite.model.FeaturedRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface FeaturedPaymentRepository extends JpaRepository<FeaturedPayment, Long> {
    Optional<FeaturedPayment> findByTransactionId(String transactionId);

    @Query("""
                SELECT COALESCE(SUM(f.amount), 0)
                FROM FeaturedPayment f
                WHERE f.status = :status
            """)
    BigDecimal getTotalAmountByStatus(PaymentStatus status);

    @Query("""
            SELECT COALESCE(SUM(f.amount), 0)
            FROM FeaturedPayment f
            WHERE f.status = com.ecommerce.ecommercewebsite.enums.PaymentStatus.SUCCESS
            AND f.paidAt BETWEEN :startDate AND :endDate
            """)
    BigDecimal getSuccessfulAmountBetween(
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    Optional<FeaturedPayment> findByFeaturedRequest(FeaturedRequest featuredRequest);
}
