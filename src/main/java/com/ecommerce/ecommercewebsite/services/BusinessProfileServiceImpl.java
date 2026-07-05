package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.BusinessProfileRequestDTO;
import com.ecommerce.ecommercewebsite.dto.VendorResponseDTO;
import com.ecommerce.ecommercewebsite.enums.ApprovalStatus;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.mappers.VendorMapper;
import com.ecommerce.ecommercewebsite.model.BusinessDocument;
import com.ecommerce.ecommercewebsite.model.BusinessProfile;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.BusinessProfileRepository;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessProfileServiceImpl implements BusinessProfileService {
    @Autowired
    private VendorMapper vendorMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BusinessProfileRepository businessProfileRepository;


    @Override
    public VendorResponseDTO vendorOnboarding(String email, BusinessProfileRequestDTO businessProfileRequestDTO) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        if (businessProfileRepository.findByUser(user).isPresent()) {
            throw new ApiException(AuthErrorCode.PROFILE_ALREADY_EXISTS);
        }
        BusinessProfile businessProfile = new BusinessProfile();
        businessProfile.setBusinessName(businessProfileRequestDTO.getBusinessName());
        businessProfile.setBusinessAddress(businessProfileRequestDTO.getBusinessAddress());
        businessProfile.setBusinessDescription(businessProfileRequestDTO.getBusinessDescription());
        businessProfile.setCategoryId(businessProfileRequestDTO.getCategoryId());
        businessProfile.setBusinessPhone(businessProfileRequestDTO.getBusinessPhone());
        businessProfile.setBusinessEmail(businessProfileRequestDTO.getBusinessEmail());
        businessProfile.setBusinessWebsite(businessProfileRequestDTO.getBusinessWebsite());
        businessProfile.setUser(user);
        businessProfile.setProfileCompleted(true);
        BusinessDocument document = new BusinessDocument();
        document.setDocumentType(businessProfileRequestDTO.getDocumentType());
        try {
            document.setDocument(businessProfileRequestDTO.getDocument().getBytes());
        } catch (Exception e) {
            throw new ApiException(AuthErrorCode.PROFILE_UPLOAD_FAILURE);
        }
        businessProfile.setBusinessDocument(document);

        BusinessProfile savedProfile = businessProfileRepository.save(businessProfile);
        VendorResponseDTO vendorResponseDTO = vendorMapper.map(savedProfile);

        return vendorResponseDTO;
    }

    @Override
    public boolean isBusinessProfileCompleted(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        boolean status = businessProfileRepository.findByUser(user)
                .map(BusinessProfile::isProfileCompleted)
                .orElse(false);
        System.out.println("Business Status = " + status);
        return status;
    }
}
