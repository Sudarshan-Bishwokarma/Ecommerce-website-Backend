package com.ecommerce.ecommercewebsite.repositories;

import com.ecommerce.ecommercewebsite.model.BusinessDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessDocumentRepository extends JpaRepository<BusinessDocument, Integer> {
}
