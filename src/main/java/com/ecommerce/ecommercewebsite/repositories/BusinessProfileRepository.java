package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.enums.ApprovalStatus;
import com.ecommerce.ecommercewebsite.model.BusinessProfile;


import com.ecommerce.ecommercewebsite.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessProfileRepository extends JpaRepository<BusinessProfile, Long> {
    Optional<BusinessProfile> findByUser(User user);

    Page<BusinessProfile> findByApprovalStatus(ApprovalStatus approvalStatus, Pageable pageable);
}
