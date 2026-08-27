package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.ProductResponseDTO;
import com.ecommerce.ecommercewebsite.dto.VendorResponseDTO;
import com.ecommerce.ecommercewebsite.dto.users.ProductDetailResponseDTO;
import com.ecommerce.ecommercewebsite.dto.users.ProductVariantDetailResponseDTO;
import com.ecommerce.ecommercewebsite.dto.users.VendorSummaryResponseDTO;
import com.ecommerce.ecommercewebsite.model.BusinessProfile;
import com.ecommerce.ecommercewebsite.model.Product;
import com.ecommerce.ecommercewebsite.model.ProductVariant;
import com.ecommerce.ecommercewebsite.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class ProductDetailMapper {
    public ProductDetailResponseDTO mapToDTO(Product product) {
        ProductDetailResponseDTO dto = new ProductDetailResponseDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductDescription(product.getProductDescription());
        if (product.getProductImage() != null) {
            String base64 = Base64.getEncoder().encodeToString(product.getProductImage());
            dto.setProductImageBase64(base64);
        }
        dto.setProductPrice(product.getDisplayPrice());
        dto.setCategoryName(product.getCategory().getCategoryName());
        dto.setDistrictName(product.getDistrict().getDistrictName());
        dto.setHasVariants(product.isHasVariants());
        dto.setStocks(product.getStock());
        //  mapping  vendor  details
        User vendor = product.getVendor();
        BusinessProfile vendorProfile = vendor.getBusinessProfile();
        VendorSummaryResponseDTO vendorSummary = new VendorSummaryResponseDTO();
        vendorSummary.setVendorId(vendor.getId());
        vendorSummary.setBusinessName(vendorProfile.getBusinessName());
        vendorSummary.setBusinessAddress(vendorProfile.getBusinessAddress());
        vendorSummary.setBusinessDescription(vendorProfile.getBusinessDescription());
        vendorSummary.setBusinessPhone(vendorProfile.getBusinessPhone());
        vendorSummary.setBusinessEmail(vendorProfile.getBusinessEmail());
        vendorSummary.setBusinessWebsite(vendorProfile.getBusinessWebsite());
        dto.setVendor(vendorSummary);
        // mapping variant details
        List<ProductVariantDetailResponseDTO> variantDetails = new ArrayList<>();
        for (ProductVariant variant : product.getProductVariants()) {
            ProductVariantDetailResponseDTO variantDetail = new ProductVariantDetailResponseDTO();
            variantDetail.setId(variant.getId());
            variantDetail.setSize(variant.getSize());
            variantDetail.setPrice(variant.getPrice());
            variantDetail.setColor(variant.getColor());
            variantDetail.setStock(variant.getStock());
            variantDetail.setSku(variant.getSku());
            if (variant.getImage() != null) {
                String base64 = Base64.getEncoder().encodeToString(variant.getImage());
                variantDetail.setVariantImageBase64(base64);
            }
            variantDetails.add(variantDetail);
        }
        dto.setVariants(variantDetails);

        return dto;


    }
}
