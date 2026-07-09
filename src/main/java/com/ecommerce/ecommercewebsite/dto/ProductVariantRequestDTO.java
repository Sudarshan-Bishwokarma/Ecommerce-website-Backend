package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductVariantRequestDTO {

    private String size;
    private String color;

    private Double price;
    private Integer stock;

   
}
    