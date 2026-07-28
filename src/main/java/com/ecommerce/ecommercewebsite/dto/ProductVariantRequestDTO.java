package com.ecommerce.ecommercewebsite.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class ProductVariantRequestDTO {

    private String size;
    private String color;
    private BigDecimal price;
    private Integer stock;


}
    