package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.enums.OrderStatus;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.model.VendorOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface VendorOrderRepository extends JpaRepository<VendorOrder, Long> {
    Page<VendorOrder> findByVendor(User vendor, Pageable pageable);

    Optional<VendorOrder> findByIdAndVendor(Long id, User vendor);

    Page<VendorOrder> findByVendorAndOrder_CreatedAtBetween(User vendor, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<VendorOrder> findByVendorAndStatus(User vendor, OrderStatus status, Pageable pageable);

    Page<VendorOrder> findByVendor_Email(String email, Pageable pageable);

    Long countByVendorAndStatus(User vendor, OrderStatus status);

}
