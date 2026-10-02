package com.ecommerce.ecommercewebsite.dto.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewResponse {
    private Long id;
    private Integer rating;
    private String comment;
    private String userName;
    private LocalDateTime createdAt;
}
