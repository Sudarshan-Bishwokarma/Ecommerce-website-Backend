package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.dto.superadmin.SuperAdminCommissionDTO;
import com.ecommerce.ecommercewebsite.dto.superadmin.SuperAdminDistrictCommissionDTO;
import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.model.VendorOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VendorOrderRepository extends JpaRepository<VendorOrder, Long> {
    Page<VendorOrder> findByVendor(User vendor, Pageable pageable);

    Long countByVendor(User vendor);

    Optional<VendorOrder> findByIdAndVendor(Long id, User vendor);

    Page<VendorOrder> findByVendorAndOrder_CreatedAtBetween(User vendor, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<VendorOrder> findByVendorAndStatus(User vendor, OrderStatus status, Pageable pageable);

    Page<VendorOrder> findByVendor_Email(String email, Pageable pageable);

    Long countByVendorAndStatus(User vendor, OrderStatus status);

    @Query("""
                SELECT COALESCE(SUM(v.commissionAmount), 0)
                FROM VendorOrder v
                WHERE v.commissionAmount IS NOT NULL
            """)
    BigDecimal getTotalCommission();

    @Query("""
            SELECT COALESCE(SUM(v.commissionAmount), 0)
            FROM VendorOrder v
            WHERE v.commissionAmount IS NOT NULL
            AND (
                (v.order.payment IS NOT NULL
                 AND v.order.payment.status = com.ecommerce.ecommercewebsite.enums.PaymentStatus.SUCCESS
                 AND v.order.payment.paidAt BETWEEN :startDate AND :endDate)
                OR
                (v.order.payment IS NULL
                 AND v.status = com.ecommerce.ecommercewebsite.enums.OrderStatus.DELIVERED
                 AND v.deliveredAt BETWEEN :startDate AND :endDate)
            )
            """)
    BigDecimal getCommissionBetween(
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    //  for vendor
    @Query("""
            SELECT COALESCE(SUM(v.vendorEarning), 0)
            FROM VendorOrder v
            WHERE v.vendor = :vendor
            AND v.vendorEarning IS NOT NULL
            """)
    BigDecimal getTotalVendorEarnings(User vendor);

    @Query("""
            SELECT new com.ecommerce.ecommercewebsite.dto.superadmin.SuperAdminCommissionDTO(
                v.id,
                v.name,
                COUNT(vo.id),
                SUM(vo.totalAmount),
                SUM(vo.commissionAmount)
            )
            FROM VendorOrder vo
            JOIN vo.vendor v
            JOIN vo.order o
            JOIN o.payment p
            WHERE vo.commissionAmount IS NOT NULL
              AND (
                  (
                      p.paymentMethod <> com.ecommerce.ecommercewebsite.enums.PaymentMethod.CASH_ON_DELIVERY
                      AND p.status = com.ecommerce.ecommercewebsite.enums.PaymentStatus.SUCCESS
                      AND o.status = com.ecommerce.ecommercewebsite.enums.OrderStatus.PAID
                      AND vo.status = com.ecommerce.ecommercewebsite.enums.OrderStatus.PAID
                      AND p.paidAt >= :startDate
                      AND p.paidAt < :endDate
                  )
                  OR
                  (
                      p.paymentMethod = com.ecommerce.ecommercewebsite.enums.PaymentMethod.CASH_ON_DELIVERY
                      AND vo.status = com.ecommerce.ecommercewebsite.enums.OrderStatus.DELIVERED
                      AND vo.deliveredAt >= :startDate
                      AND vo.deliveredAt < :endDate
                  )
              )
            GROUP BY v.id, v.name
            ORDER BY SUM(vo.commissionAmount) DESC
            """)
    List<SuperAdminCommissionDTO> getCommissionByVendor(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
            SELECT new com.ecommerce.ecommercewebsite.dto.superadmin.SuperAdminDistrictCommissionDTO(
                d.id,
                d.districtName,
                COUNT(vo.id),
                SUM(vo.totalAmount),
                SUM(vo.commissionAmount)
            )
            FROM VendorOrder vo
            JOIN vo.order o
            JOIN o.district d
            JOIN o.payment p
            WHERE vo.commissionAmount IS NOT NULL
              AND (
                  (
                      p.paymentMethod <> com.ecommerce.ecommercewebsite.enums.PaymentMethod.CASH_ON_DELIVERY
                      AND p.status = com.ecommerce.ecommercewebsite.enums.PaymentStatus.SUCCESS
                      AND o.status = com.ecommerce.ecommercewebsite.enums.OrderStatus.PAID
                      AND vo.status = com.ecommerce.ecommercewebsite.enums.OrderStatus.PAID
                      AND p.paidAt >= :startDate
                      AND p.paidAt < :endDate
                  )
                  OR
                  (
                      p.paymentMethod = com.ecommerce.ecommercewebsite.enums.PaymentMethod.CASH_ON_DELIVERY
                      AND vo.status = com.ecommerce.ecommercewebsite.enums.OrderStatus.DELIVERED
                      AND vo.deliveredAt >= :startDate
                      AND vo.deliveredAt < :endDate
                  )
              )
            GROUP BY d.id, d.districtName
            ORDER BY SUM(vo.commissionAmount) DESC
            """)
    List<SuperAdminDistrictCommissionDTO> getCommissionByDistrict(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
