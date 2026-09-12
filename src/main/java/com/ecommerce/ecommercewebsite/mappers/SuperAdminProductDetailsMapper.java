package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.superadmin.ProductDetailsResponseDTO;
import com.ecommerce.ecommercewebsite.dto.superadmin.ProductVariantDetailsDTO;
import com.ecommerce.ecommercewebsite.dto.superadmin.VendorDetailsDTO;
import com.ecommerce.ecommercewebsite.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class SuperAdminProductDetailsMapper {
    public ProductDetailsResponseDTO mapToDTO(Product product) {

        ProductDetailsResponseDTO dto = new ProductDetailsResponseDTO();

        // Product information
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setProductDescription(product.getProductDescription());
        dto.setStatus(product.getStatus());

        // Product image
        if (product.getProductImage() != null) {
            dto.setProductImageBase64(
                    Base64.getEncoder().encodeToString(product.getProductImage())
            );
        }

        // Price & stock
        dto.setDisplayPrice(product.getDisplayPrice());
        // Category
        if (product.getCategory() != null) {
            dto.setProductCategory(product.getCategory().getCategoryName());
        }

        // District
        if (product.getDistrict() != null) {
            dto.setDistrictName(product.getDistrict().getDistrictName());
        }


        // Variants
        List<ProductVariant> productVariants = product.getProductVariants();


        if (productVariants != null && !productVariants.isEmpty()) {
            dto.setHasVariants(true);

            int totalStock = 0;
            for (ProductVariant v : productVariants) {

                if (v.getStock() != null) {
                    totalStock += v.getStock();
                }
            }
            dto.setStock(totalStock);
        } else {
            dto.setHasVariants(false);
            dto.setStock(product.getStock());
        }


        // Vendor
        if (product.getVendor() != null) {

            User vendor = product.getVendor();

            VendorDetailsDTO vendorDTO = new VendorDetailsDTO();

            // User information
            vendorDTO.setVendorId(vendor.getId());
            vendorDTO.setName(vendor.getName());
            vendorDTO.setEmail(vendor.getEmail());

            // Profile information
            if (vendor.getProfile() != null) {

                Profile profile = vendor.getProfile();

                vendorDTO.setCity(profile.getCity());
                vendorDTO.setCountry(profile.getCountry());
                vendorDTO.setNumber(profile.getNumber());

                if (profile.getProfileImage() != null) {
                    String base64 = Base64.getEncoder()
                            .encodeToString(profile.getProfileImage());

                    vendorDTO.setProfileImageBase64(base64);
                }
            }

            // Business information
            if (vendor.getBusinessProfile() != null) {
                BusinessProfile businessProfile = vendor.getBusinessProfile();
                vendorDTO.setBusinessName(businessProfile.getBusinessName());
                vendorDTO.setBusinessAddress(businessProfile.getBusinessAddress());
                vendorDTO.setBusinessPhone(businessProfile.getBusinessPhone());
                vendorDTO.setBusinessEmail(businessProfile.getBusinessEmail());
                vendorDTO.setBusinessDescription(businessProfile.getBusinessDescription());
                vendorDTO.setBusinessWebsite(businessProfile.getBusinessWebsite());
            }

            dto.setVendor(vendorDTO);
        }
        List<ProductVariantDetailsDTO> variants = new ArrayList<>();
        if (product.getProductVariants() != null && !product.getProductVariants().isEmpty()) {
            for (ProductVariant v : product.getProductVariants()) {
                ProductVariantDetailsDTO variantDTO = new ProductVariantDetailsDTO();
                variantDTO.setId(v.getId());
                variantDTO.setSize(v.getSize());
                variantDTO.setColor(v.getColor());
                variantDTO.setStock(v.getStock());
                variantDTO.setPrice(v.getPrice());
                if (v.getImage() != null) {
                    String base64 = Base64.getEncoder().encodeToString(v.getImage());
                    variantDTO.setVariantImageBase64(base64);
                }
                variants.add(variantDTO);
            }
            dto.setVariant(variants);

        }

        return dto;
    }


}
