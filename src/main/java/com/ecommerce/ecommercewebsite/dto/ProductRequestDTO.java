package com.ecommerce.ecommercewebsite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRequestDTO {
    private String productName;
    private String productDescription;
    private Long categoryId;
    private Long districtId;
    private MultipartFile productImage;
    private Double price;
    private Integer stock;
    private List<ProductVariantRequestDTO> variants;
}

