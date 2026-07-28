package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.model.Cart;
import com.ecommerce.ecommercewebsite.model.CartItem;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);

    Optional<CartItem> findByCartAndProductVariant(Cart cart, ProductVariant productVariant);

    Optional<CartItem> findByCartAndProductAndProductVariant(Cart cart, Product product, ProductVariant variant);

}
