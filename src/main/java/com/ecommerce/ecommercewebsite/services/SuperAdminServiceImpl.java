package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.VendorResponseDTO;
import com.ecommerce.ecommercewebsite.enums.ApprovalStatus;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.exception.UserNotFoundException;
import com.ecommerce.ecommercewebsite.mappers.VendorMapper;
import com.ecommerce.ecommercewebsite.model.BusinessProfile;
import com.ecommerce.ecommercewebsite.model.Role;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.BusinessProfileRepository;
import com.ecommerce.ecommercewebsite.repositories.RoleRepository;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private BusinessProfileRepository businessProfileRepository;
    @Autowired
    private VendorMapper vendorMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public String deleteVendor(Long id) {
        userRepository.deleteById(id);
        return "Admin Deleted Successfully";
    }

    @Override
    public Page<VendorResponseDTO> getAllVendors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Role role = roleRepository.findByRole("ROLE_VENDOR");
        Page<BusinessProfile> businessProfilePage = businessProfileRepository.findAll(pageable);
        return businessProfilePage.map(vendorMapper::map);
    }

    @Override
    public String approveVendor(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(AuthErrorCode.USER_NOT_FOUND));
        if (!user.isVerified()) {
            throw new ApiException(AuthErrorCode.NOT_VERIFIED);
        }
        BusinessProfile businessProfile = businessProfileRepository.findByUser(user).orElseThrow(() -> new ApiException(AuthErrorCode.BUSINESS_PROFILE_NOT_FOUND));
        if (businessProfile.getApprovalStatus() != ApprovalStatus.APPROVED) {
            businessProfile.setApprovalStatus(ApprovalStatus.APPROVED);
            businessProfileRepository.save(businessProfile);
        }
        return " Approved Successfully";
    }

    @Override
    public Page<VendorResponseDTO> getAllPendingVendors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BusinessProfile> profiles = businessProfileRepository.findByApprovalStatus(ApprovalStatus.PENDING, pageable);
        return profiles.map(vendorMapper::map);

    }


}
