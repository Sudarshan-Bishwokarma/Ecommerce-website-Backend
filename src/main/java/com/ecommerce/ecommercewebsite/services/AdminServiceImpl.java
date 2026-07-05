package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.VendorResponseDTO;
import com.ecommerce.ecommercewebsite.dto.AdminUpdateDTO;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.exception.EmailAlreadyExistsException;
import com.ecommerce.ecommercewebsite.exception.ImagenNotFoundException;
import com.ecommerce.ecommercewebsite.model.Profile;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.ProfileRepository;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public VendorResponseDTO editAdmin(AdminUpdateDTO updateDTO, MultipartFile file, Long id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().isEmpty()) {
            if (userRepository.existsByEmail(updateDTO.getEmail())) {
                throw new EmailAlreadyExistsException("Email already  user  by other user");
            }
            user.setEmail(updateDTO.getEmail());
        }
        Profile profile = profileRepository.findById(user.getProfile().getUser().getId()).orElseThrow(() -> new ApiException(AuthErrorCode.PROFILE_NOT_FOUND));
        try {
            profile.setProfileImage(file.getBytes());
        } catch (Exception e) {
            throw new ImagenNotFoundException("File upload error");

        }

        profile.setCity(updateDTO.getCity());
        profile.setNumber(updateDTO.getNumber());
        Profile updatedUser = profileRepository.save(profile);
        System.out.println("Admin has been successfully updated ");
        VendorResponseDTO responseDTO = mapToDTO(updatedUser);

        return responseDTO;
    }

    // Helper class
    private VendorResponseDTO mapToDTO(Profile profile) {
        VendorResponseDTO responseDTO = new VendorResponseDTO();
        responseDTO.setId(profile.getUser().getId());
        responseDTO.setEmail(profile.getUser().getEmail());
        responseDTO.setCity(profile.getCity());
        responseDTO.setNumber(profile.getNumber());
        return responseDTO;
    }
}
