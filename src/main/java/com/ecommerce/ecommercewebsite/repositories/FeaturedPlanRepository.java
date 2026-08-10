package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.model.FeaturedPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeaturedPlanRepository extends JpaRepository<FeaturedPlan, Long> {

    List<FeaturedPlan> findByActiveTrue();
}

