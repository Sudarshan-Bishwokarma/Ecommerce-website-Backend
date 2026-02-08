package com.ecommerce.ecommercewebsite.dataCentralization;

import com.ecommerce.ecommercewebsite.model.Role;
import com.ecommerce.ecommercewebsite.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
@Component
public class RoleCreation {
    @Autowired
    RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        if (roleRepository.count() == 0) {
            Role superRole = new Role();
            superRole.setRole("ROLE_SUPER_ADMIN");

            Role adminRole = new Role();
            adminRole.setRole("ROLE_ADMIN");

            Role userRole = new Role();
            userRole.setRole("ROLE_USER");


            // Save all roles in one call
            roleRepository.saveAll(Arrays.asList(superRole, adminRole, userRole));
            System.out.println("Default roles inserted successfully!");
        } else {
            System.out.println("Roles already exist in database.");
        }

    }

}

//   ths method will run automatically  when the  all the bean object is created and  all dependencies are created
