package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.JwtResponseDto;
import com.ecommerce.ecommercewebsite.model.Role;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.RoleRepository;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import com.ecommerce.ecommercewebsite.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class OAuth2UserServiceImpl implements OAuth2UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    RoleRepository roleRepository;
    @Override
    public JwtResponseDto processGoogleLogin(OAuth2User oAuth2User) {
        // Extract user information from the user
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        //heck if the user  exits in the database or create a new user
        Optional<User> OptionalUser = userRepository.findByEmail(email);
        User user;
        if (OptionalUser.isPresent()) {
            user = OptionalUser.get();
        } else {
            user = new User();
            user.setName(name);
            user.setEmail(email);
            Role role = roleRepository.findByRole("ROLE_USER");
            user.setRole(role);
            userRepository.save(user);
            System.out.println(" GoogleUser is Saved in the database");
            // generate the token
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().getRole());
        return new JwtResponseDto(token, email,user.getRole().getRole());
    }
}
