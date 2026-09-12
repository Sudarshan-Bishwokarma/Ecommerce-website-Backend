package com.ecommerce.ecommercewebsite.mappers;

import com.ecommerce.ecommercewebsite.dto.vendor.BusinessDocumentDetailsDTO;
import com.ecommerce.ecommercewebsite.dto.vendor.BusinessProfileDetailsDTO;
import com.ecommerce.ecommercewebsite.dto.vendor.VendorDetailsResponseDTO;
import com.ecommerce.ecommercewebsite.dto.vendor.VendorProfileDetailsDTO;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.model.*;
import com.ecommerce.ecommercewebsite.repositories.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class VendorDetailsMapper {

    public VendorDetailsResponseDTO map(User user) {

        VendorDetailsResponseDTO dto = new VendorDetailsResponseDTO();

        // Vendor information
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setVerified(user.isVerified());

        //  it is optional
        Profile profile = user.getProfile();

        if (profile != null) {
            VendorProfileDetailsDTO profileDTO = new VendorProfileDetailsDTO();

            profileDTO.setId(profile.getId());
            profileDTO.setCity(profile.getCity());
            profileDTO.setCountry(profile.getCountry());
            profileDTO.setNumber(profile.getNumber());
            profileDTO.setProfileStatus(profile.getProfileStatus());

            if (profile.getProfileImage() != null) {
                profileDTO.setBase64profile(
                        Base64.getEncoder()
                                .encodeToString(profile.getProfileImage())
                );
            }

            dto.setProfile(profileDTO);
        }

        // Business profile
        BusinessProfile businessProfile = user.getBusinessProfile();

        if (businessProfile != null) {
            BusinessProfileDetailsDTO businessDTO = new BusinessProfileDetailsDTO();

            businessDTO.setId(businessProfile.getId());
            businessDTO.setBusinessName(businessProfile.getBusinessName());
            businessDTO.setBusinessAddress(businessProfile.getBusinessAddress());
            businessDTO.setBusinessDescription(
                    businessProfile.getBusinessDescription()
            );

            businessDTO.setBusinessPhone(businessProfile.getBusinessPhone());
            businessDTO.setBusinessEmail(businessProfile.getBusinessEmail());
            businessDTO.setBusinessWebsite(businessProfile.getBusinessWebsite());
            businessDTO.setApprovalStatus(businessProfile.getApprovalStatus());
            businessDTO.setProfileCompleted(
                    businessProfile.isProfileCompleted()
            );

            dto.setBusinessProfile(businessDTO);

            // Business document
            BusinessDocument document = businessProfile.getBusinessDocument();

            if (document != null) {
                BusinessDocumentDetailsDTO documentDTO = new BusinessDocumentDetailsDTO();

                documentDTO.setId(document.getId());
                documentDTO.setDocumentType(document.getDocumentType());
                documentDTO.setContentType(document.getContentType());
                documentDTO.setFileName(document.getFileName());
                documentDTO.setUploadedAt(document.getUploadedAt());

                if (document.getDocument() != null) {
                    documentDTO.setBase64Document(Base64.getEncoder().encodeToString(document.getDocument())
                    );
                }

                dto.setBusinessDocument(documentDTO);
            }
        }

        return dto;
    }
}
