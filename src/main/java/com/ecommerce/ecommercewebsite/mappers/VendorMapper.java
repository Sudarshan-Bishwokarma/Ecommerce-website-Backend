package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.VendorResponseDTO;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.model.BusinessDocument;
import com.ecommerce.ecommercewebsite.model.BusinessProfile;
import com.ecommerce.ecommercewebsite.model.Profile;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.ProfileRepository;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;

@Component
public class VendorMapper {

    public VendorResponseDTO map(BusinessProfile businessProfile) {
        User user = businessProfile.getUser();
        Profile profile = user.getProfile();
        VendorResponseDTO vendorResponseDTO = new VendorResponseDTO();
        // mapping  user info
        vendorResponseDTO.setId(user.getId());
        vendorResponseDTO.setName(user.getName());
        vendorResponseDTO.setEmail(user.getEmail());
        if (profile != null) {
            vendorResponseDTO.setCity(profile.getCity());
            vendorResponseDTO.setNumber(profile.getNumber());
        } else {
            vendorResponseDTO.setCity(null);
            vendorResponseDTO.setNumber(null);
        }
        if (profile != null && profile.getProfileImage() != null) {
            String base64 = Base64.getEncoder()
                    .encodeToString(profile.getProfileImage());

            vendorResponseDTO.setBase64profile(base64);
        }
        // mapping business info
        vendorResponseDTO.setBusinessName(businessProfile.getBusinessName());
        vendorResponseDTO.setCategoryId(businessProfile.getCategoryId());
        vendorResponseDTO.setBusinessAddress(businessProfile.getBusinessAddress());
        vendorResponseDTO.setBusinessDescription(businessProfile.getBusinessDescription());
        vendorResponseDTO.setBusinessPhone(businessProfile.getBusinessPhone());
        vendorResponseDTO.setBusinessEmail(businessProfile.getBusinessEmail());
        vendorResponseDTO.setBusinessWebsite(businessProfile.getBusinessWebsite());
        vendorResponseDTO.setApprovalStatus(businessProfile.getApprovalStatus());
        vendorResponseDTO.setBusinessProfileCompleted(businessProfile.isProfileCompleted());
        return vendorResponseDTO;
    }

}   
