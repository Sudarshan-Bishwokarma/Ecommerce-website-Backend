package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.JwtResponseDto;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.enums.ProfileStatus;
import com.ecommerce.ecommercewebsite.exception.ApiException;
import com.ecommerce.ecommercewebsite.model.Profile;
import com.ecommerce.ecommercewebsite.model.Role;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.ProfileRepository;
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
    @Autowired
    ProfileRepository profileRepository;


    @Override
    public JwtResponseDto processGoogleLogin(OAuth2User oAuth2User) {

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user;

        if (optionalUser.isPresent()) {

            // Existing account
            user = optionalUser.get();

            // Google Login is only allowed for normal users
            if (!user.getRole().getRole().equals("ROLE_USER")) {
                throw new ApiException(AuthErrorCode.GOOGLE_LOGIN_USER_ONLY);
            }

        } else {

            // New Google account
            user = new User();
            user.setName(name);
            user.setEmail(email);

            // Every new Google account becomes USER
            Role role = roleRepository.findByRole("ROLE_USER");
            user.setRole(role);

            userRepository.save(user);

            System.out.println("Google User is saved in the database");
        }

        Profile profile = profileRepository.findByUser(user).orElse(null);

        ProfileStatus status;

        if (profile == null) {
            status = ProfileStatus.PENDING;
        } else {
            status = profile.getProfileStatus();
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().getRole()
        );

        return new JwtResponseDto(
                email,
                token,
                user.getRole().getRole(),
                status
        );
    }
}
