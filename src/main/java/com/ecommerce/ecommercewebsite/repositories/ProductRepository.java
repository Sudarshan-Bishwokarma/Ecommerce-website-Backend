package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory_id(Long categoryId);

    public Optional<Product> findByProductName(String productName);

    List<Product> findByProductPriceBetween(Double minPrice, Double maxPrice);

    Page<Product> findByDistrict_Id(Long districtId, Pageable pageable);

    Page<Product> findAllByOrderByProductPriceAsc(Pageable pageable);

    Page<Product> findAllByOrderByProductPriceDesc(Pageable pageable);

    Page<Product> findAllByOrderByProductNameAsc(Pageable pageable);

    Page<Product> findAllByOrderByProductNameDesc(Pageable pageable);

    // sort  method  for admin  only
    Page<Product> findByAdmin_IdOrderByProductPriceAsc(Long id, Pageable pageable);

    Page<Product> findByAdmin_IdOrderByProductPriceDesc(Long id, Pageable pageable);

    Page<Product> findByAdmin_IdOrderByProductNameAsc(Long id, Pageable pageable);

    Page<Product> findByAdmin_IdOrderByProductNameDesc(Long id, Pageable pageable);

    Page<Product> findByAdmin_Id(Long id, Pageable pageable);

    Page<Product> findByAdmin_IdAndDistrict_Id(Long id, Long districtId, Pageable pageable);

    Page<Product> findByAdmin_IdAndDistrict_IdAndCategory_Id(Long adminId, Long districtId, Long categoryId, Pageable pageable);

    Page<Product> findByAdmin_IdAndCategory_Id(Long adminId, Long categoryId, Pageable pageable);
}

