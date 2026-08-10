package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.enums.FeaturedRequestStatus;
import com.ecommerce.ecommercewebsite.model.FeaturedRequest;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Optional;

public interface FeaturedRequestRepository extends JpaRepository<FeaturedRequest, Long> {

    boolean existsByProductAndStatus(Product product, FeaturedRequestStatus status);

    Page<FeaturedRequest> findByStatus(FeaturedRequestStatus status, Pageable pageable);

    List<FeaturedRequest> findByStatus(FeaturedRequestStatus status);

    Page<FeaturedRequest> findByVendor(User vendor, Pageable pageable);

    FeaturedRequest findTopByProductOrderByIdDesc(Product product);

    Optional<FeaturedRequest> findByTransactionUuid(String transactionUuid);

    
}
