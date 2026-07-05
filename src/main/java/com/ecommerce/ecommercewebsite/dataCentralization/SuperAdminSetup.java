package com.ecommerce.ecommercewebsite.dataCentralization;

import com.ecommerce.ecommercewebsite.model.Profile;
import com.ecommerce.ecommercewebsite.model.Role;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.RoleRepository;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SuperAdminSetup {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        boolean status = userRepository.existsByEmail("baraldrashan0@gmail.com");
        if (!status) {
            User user = new User();
            Profile profile = new Profile();
            user.setEmail("baraldrashan0@gmail.com");
            user.setName("Sudarshan Bishwokarma");
            user.setPassword(passwordEncoder.encode("darshan@123"));
            profile.setCity("Kathmandu");
            profile.setNumber("9869779168");
            Role role = roleRepository.findByRole("ROLE_SUPER_ADMIN");
            user.setRole(role);
            userRepository.save(user);
            System.out.println("SupeAdmin created successfully");
        } else {
            System.out.println("SuperAdmin already exists");
        }
    }

}
