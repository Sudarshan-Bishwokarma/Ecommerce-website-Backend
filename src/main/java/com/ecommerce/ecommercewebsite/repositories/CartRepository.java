package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.model.Cart;
import com.ecommerce.ecommercewebsite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
