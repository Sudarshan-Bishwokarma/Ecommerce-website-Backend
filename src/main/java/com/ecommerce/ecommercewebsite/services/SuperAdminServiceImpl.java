package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.AdminRequestDTO;
import com.ecommerce.ecommercewebsite.dto.AdminResponseDTO;
import com.ecommerce.ecommercewebsite.exception.EmailAlreadyExistsException;
import com.ecommerce.ecommercewebsite.exception.UserNotFoundException;
import com.ecommerce.ecommercewebsite.model.Role;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.RoleRepository;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    PasswordEncoder passwordEncoder;

    @Override
    public AdminResponseDTO addAdmin(AdminRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email Already Exists");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCity(request.getCity());
        user.setNumber(request.getNumber());
        Role role = roleRepository.findByRole("ROLE_ADMIN");
        user.setRole(role);
        user.setVerified(true);
        User savedAdmin = userRepository.save(user);
        System.out.println("Admin Added Successfully");
        // prepare for admin response dto

        AdminResponseDTO adminResponseDTO = mapToDTO(savedAdmin);
        return adminResponseDTO;
    }

    @Override
    public AdminResponseDTO updateAdmin(AdminRequestDTO request, Long id) {
        User admin = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        admin.setName(request.getName());
        admin.setEmail(request.getEmail());
        admin.setCity(request.getCity());
        admin.setNumber(request.getNumber());
        User savedAdmin = userRepository.save(admin);
        System.out.println("Admin Updated Successfully");
        AdminResponseDTO adminResponseDTO = mapToDTO(savedAdmin);
        return adminResponseDTO;

    }

    @Override
    public String deleteAdmin(Long id) {
        userRepository.deleteById(id);
        return "Admin Deleted Successfully";
    }

    @Override
    public List<AdminResponseDTO> getAllAdmins() {
        Role adminRole = roleRepository.findByRole("ROLE_ADMIN");
        List<User> allAdmins = userRepository.findAllByRole(adminRole);
        if (allAdmins.isEmpty()) {
            throw new UserNotFoundException("Admins not found");
        }
        List<AdminResponseDTO> adminResponseDTO = new ArrayList<>();
        for (User user : allAdmins) {
            AdminResponseDTO response = mapToDTO(user);
            adminResponseDTO.add(response);
        }
        return adminResponseDTO;

    }

    //  helper class
    private AdminResponseDTO mapToDTO(User user) {
        AdminResponseDTO responseDTO = new AdminResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setName(user.getName());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setCity(user.getCity());
        responseDTO.setNumber(user.getNumber());
        return responseDTO;
    }
}
