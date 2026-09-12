package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.dto.superadmin.SuperAdminSalesDTO;
import com.ecommerce.ecommercewebsite.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    @Query("""
                SELECT COUNT(oi) > 0
                FROM OrderItem oi
                WHERE oi.product.productId = :productId
                AND oi.vendorOrder.status IN (
                    com.ecommerce.ecommercewebsite.enums.OrderStatus.PENDING_PAYMENT,
                    com.ecommerce.ecommercewebsite.enums.OrderStatus.PAID,
                    com.ecommerce.ecommercewebsite.enums.OrderStatus.PROCESSING,
                    com.ecommerce.ecommercewebsite.enums.OrderStatus.SHIPPED
                )
            """)
    boolean existsActiveOrderForProduct(@Param("productId") Long productId);

    @Query("""
            SELECT new com.ecommerce.ecommercewebsite.dto.superadmin.SuperAdminSalesDTO(
                p.productId,
                oi.productName,
                SUM(oi.quantity),
                SUM(oi.priceAtPurchase * oi.quantity)
            )
            FROM OrderItem oi
            JOIN oi.product p
            JOIN oi.vendorOrder vo
            JOIN vo.order o
            WHERE (
                (
                    o.payment IS NOT NULL
                    AND o.payment.status =
                        com.ecommerce.ecommercewebsite.enums.PaymentStatus.SUCCESS
                )
                OR
                (
                    o.payment IS NULL
                    AND vo.status =
                        com.ecommerce.ecommercewebsite.enums.OrderStatus.DELIVERED
                )
            )
            GROUP BY p.productId, oi.productName
            """)
    List<SuperAdminSalesDTO> getSalesByProduct();
}
