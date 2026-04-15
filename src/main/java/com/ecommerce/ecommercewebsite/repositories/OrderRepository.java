package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.model.Order;
import com.ecommerce.ecommercewebsite.model.OrderStatus;
import com.ecommerce.ecommercewebsite.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUser(User user, Pageable pageable);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    Page<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    

}
