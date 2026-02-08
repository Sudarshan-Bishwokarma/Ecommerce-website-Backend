package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.AdminResponseDTO;
import com.ecommerce.ecommercewebsite.dto.AdminUpdateDTO;
import com.ecommerce.ecommercewebsite.exception.EmailAlreadyExistsException;
import com.ecommerce.ecommercewebsite.exception.ImagenNotFoundException;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public AdminResponseDTO editAdmin(AdminUpdateDTO updateDTO, MultipartFile file, Long id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().isEmpty()) {
            if (userRepository.existsByEmail(updateDTO.getEmail())) {
                throw new EmailAlreadyExistsException("Email already  user  by other user");
            }
            user.setEmail(updateDTO.getEmail());
        }
        try {
            user.setProfile(file.getBytes());
        } catch (Exception e) {
            throw new ImagenNotFoundException("File upload error");

        }

        user.setCity(updateDTO.getCity());
        user.setNumber(updateDTO.getNumber());
        User updatedUser = userRepository.save(user);
        System.out.println("Admin has been successfully updated ");
        AdminResponseDTO responseDTO = mapToDTO(updatedUser);

        return responseDTO;
    }
    
    // Helper class
    private AdminResponseDTO mapToDTO(User user) {
        AdminResponseDTO responseDTO = new AdminResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setCity(user.getCity());
        responseDTO.setNumber(user.getNumber());
        return responseDTO;
    }
}
