package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.model.Role;
import com.ecommerce.ecommercewebsite.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    // Find user by email
    Optional<User> findByEmail(String email);

    // Find user by username (used in MyUserDetailsService)
    Optional<User> findByName(String username);

    public boolean existsByEmail(String email);

    public Page<User> findAllByRole(Role role, Pageable pageable);


}
