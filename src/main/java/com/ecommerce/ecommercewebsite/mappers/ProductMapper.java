package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.ProductResponseDTO;
import com.ecommerce.ecommercewebsite.dto.ProfileResponseDTO;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.model.ProductVariants;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;

@Component
public class ProductMapper {

    public ProductResponseDTO mapToDTO(Product product) {

        ProductResponseDTO dto = new ProductResponseDTO();

        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductDescription(product.getProductDescription());
        dto.setStatus(product.getStatus());
        dto.setProductCategory(product.getCategory().getCategoryName());
        dto.setDistrictName(product.getDistrict().getDistrictName());
        if (product.getProductImage() != null) {
            String base64 = Base64.getEncoder()
                    .encodeToString(product.getProductImage());
            dto.setProductImageBase64(base64);
        }

        List<ProductVariants> variants = product.getProductVariants();


        // cas 1:  has variants
        if (variants != null && !variants.isEmpty()) {
            dto.setHasVariants(true);

            double minPrice = Double.MAX_VALUE;
            int totalStock = 0;

            for (ProductVariants v : variants) {
                if (v.getPrice() != null && v.getPrice() < minPrice) {
                    minPrice = v.getPrice();
                }

                if (v.getStock() != null) {
                    totalStock += v.getStock();
                }
            }

            dto.setProductPrice(minPrice);
            dto.setStock(totalStock);
        }

        //  case 2 : no variants

        else {
            dto.setHasVariants(false);
            dto.setProductPrice(product.getPrice());

            dto.setStock(product.getStock());
        }

        return dto;
    }
}