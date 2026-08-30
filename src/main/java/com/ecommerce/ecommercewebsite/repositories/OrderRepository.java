package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.dto.MonthlyOrderDTO;
import com.ecommerce.ecommercewebsite.model.Order;
import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import com.ecommerce.ecommercewebsite.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByCustomer(User customer, Pageable pageable);

    Page<Order> findByCustomerAndStatus(User customer, OrderStatus status, Pageable pageable);

    Optional<Order> findByTransactionUuid(String transactionUuid);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    Page<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    long countByStatus(OrderStatus status);

    @Query("""
            SELECT new com.ecommerce.ecommercewebsite.dto.MonthlyOrderDTO(
                   MONTH(o.createdAt),
                   COUNT(o)
            )
            FROM Order o
            GROUP BY MONTH(o.createdAt)
            ORDER BY MONTH(o.createdAt)
            """)
    List<MonthlyOrderDTO> getMonthlyOrders();
}
