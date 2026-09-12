package com.ecommerce.ecommercewebsite.services.vendor;

import com.ecommerce.ecommercewebsite.dto.vendor.VendorProfileResponseDTO;
import com.ecommerce.ecommercewebsite.dto.vendor.VendorProfileUpdateRequestDTO;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.enums.ProductErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.model.BusinessDocument;
import com.ecommerce.ecommercewebsite.model.BusinessProfile;
import com.ecommerce.ecommercewebsite.model.Profile;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class VendorProfileServiceImpl implements VendorProfileService {

    private final ProfileRepository profileRepository;

    public VendorProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public VendorProfileResponseDTO getVendorProfile(User vendor) {
        Profile profile = vendor.getProfile();
        BusinessProfile businessProfile = vendor.getBusinessProfile();

        VendorProfileResponseDTO dto = new VendorProfileResponseDTO();

        // User information
        dto.setVendorId(vendor.getId());
        dto.setName(vendor.getName());
        dto.setEmail(vendor.getEmail());

        // Personal profile information
        if (profile != null) {
            dto.setCity(profile.getCity());
            dto.setCountry(profile.getCountry());
            dto.setNumber(profile.getNumber());
            dto.setProfileStatus(profile.getProfileStatus());

            if (profile.getProfileImage() != null) {
                dto.setProfileImageBase64(
                        Base64.getEncoder().encodeToString(profile.getProfileImage())
                );
            }
        }

        // Business profile information
        if (businessProfile != null) {

            dto.setBusinessProfileId(businessProfile.getId());
            dto.setBusinessName(businessProfile.getBusinessName());
            dto.setBusinessAddress(businessProfile.getBusinessAddress());
            dto.setBusinessDescription(businessProfile.getBusinessDescription());
            dto.setCategoryId(businessProfile.getCategoryId());
            dto.setBusinessPhone(businessProfile.getBusinessPhone());
            dto.setBusinessEmail(businessProfile.getBusinessEmail());
            dto.setBusinessWebsite(businessProfile.getBusinessWebsite());

            dto.setApprovalStatus(businessProfile.getApprovalStatus());
            dto.setProfileCompleted(businessProfile.isProfileCompleted());

            // Business document information
            BusinessDocument document = businessProfile.getBusinessDocument();

            if (document != null) {
                dto.setDocumentType(
                        document.getDocumentType() != null
                                ? document.getDocumentType().name()
                                : null
                );

                dto.setDocumentFileName(document.getFileName());
                dto.setDocumentContentType(document.getContentType());
            }
        }

        return dto;

    }

    @Override
    public VendorProfileResponseDTO updateVendorProfile(
            User vendor,
            VendorProfileUpdateRequestDTO request) {

        if (request.getName() != null &&
                !request.getName().trim().isEmpty()) {
            vendor.setName(request.getName().trim());
        }

        Profile profile = vendor.getProfile();

        if (profile == null) {
            profile = new Profile();
            profile.setUser(vendor);
            vendor.setProfile(profile);
        }

        if (request.getNumber() != null) {
            profile.setNumber(request.getNumber().trim());
        }

        if (request.getCity() != null) {
            profile.setCity(request.getCity().trim());
        }

        if (request.getCountry() != null) {
            profile.setCountry(request.getCountry().trim());
        }

        if (request.getProfileImage() != null &&
                !request.getProfileImage().isEmpty()) {

            try {
                profile.setProfileImage(
                        request.getProfileImage().getBytes()
                );
            } catch (Exception e) {
                throw new ApiException(
                        ProductErrorCode.IMAGE_UPLOADED_FAILED
                );
            }
        }

        Profile savedProfile = profileRepository.save(profile);

        VendorProfileResponseDTO dto =
                new VendorProfileResponseDTO();

        dto.setVendorId(vendor.getId());
        dto.setName(vendor.getName());
        dto.setEmail(vendor.getEmail());

        dto.setCity(savedProfile.getCity());
        dto.setCountry(savedProfile.getCountry());
        dto.setNumber(savedProfile.getNumber());
        dto.setProfileStatus(savedProfile.getProfileStatus());

        if (savedProfile.getProfileImage() != null) {
            dto.setProfileImageBase64(
                    Base64.getEncoder().encodeToString(
                            savedProfile.getProfileImage()
                    )
            );
        }

        BusinessProfile businessProfile =
                vendor.getBusinessProfile();

        if (businessProfile != null) {

            dto.setBusinessProfileId(
                    businessProfile.getId()
            );

            dto.setBusinessName(
                    businessProfile.getBusinessName()
            );

            dto.setBusinessAddress(
                    businessProfile.getBusinessAddress()
            );

            dto.setBusinessDescription(
                    businessProfile.getBusinessDescription()
            );

            dto.setCategoryId(
                    businessProfile.getCategoryId()
            );

            dto.setBusinessPhone(
                    businessProfile.getBusinessPhone()
            );

            dto.setBusinessEmail(
                    businessProfile.getBusinessEmail()
            );

            dto.setBusinessWebsite(
                    businessProfile.getBusinessWebsite()
            );

            dto.setApprovalStatus(
                    businessProfile.getApprovalStatus()
            );

            dto.setProfileCompleted(
                    businessProfile.isProfileCompleted()
            );

            BusinessDocument document =
                    businessProfile.getBusinessDocument();

            if (document != null) {

                dto.setDocumentType(
                        document.getDocumentType() != null
                                ? document.getDocumentType().name()
                                : null
                );

                dto.setDocumentFileName(
                        document.getFileName()
                );

                dto.setDocumentContentType(
                        document.getContentType()
                );
            }
        }

        return dto;
    }
}
