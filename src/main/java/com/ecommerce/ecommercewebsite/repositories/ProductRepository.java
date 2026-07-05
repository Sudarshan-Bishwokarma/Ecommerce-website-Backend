package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByDistrict_id(Long categoryId);

    Page<Product> findByDistrict_id(Long categoryId, Pageable pageable);

    public Optional<Product> findByProductName(String productName);

    Optional<Product> findByProductNameIgnoreCaseAndVendor_Id(String productName, long vendorId);

    Page<Product> findAllByOrderByProductNameAsc(Pageable pageable);

    Page<Product> findAllByOrderByProductNameDesc(Pageable pageable);

    Page<Product> findByVendor_Id(Long id, Pageable pageable);

    Page<Product> findByVendor_IdAndDistrict_Id(Long id, Long districtId, Pageable pageable);

    Page<Product> findByVendor_IdAndDistrict_IdAndCategory_Id(Long adminId, Long districtId, Long categoryId, Pageable pageable);

    Page<Product> findByVendor_IdAndCategory_Id(Long adminId, Long categoryId, Pageable pageable);


}

