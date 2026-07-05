package com.ecommerce.ecommercewebsite.security;

import com.ecommerce.ecommercewebsite.dto.JwtResponseDto;
import com.ecommerce.ecommercewebsite.services.OAuth2UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private OAuth2UserService oAuth2UserService;

    @Override
    //onAuthenticationSuccess() is automatically called by Spring Security.
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
//oAuth2User stores all logged-in Google user info.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        JwtResponseDto jwtResponse = oAuth2UserService.processGoogleLogin(oAuth2User);


        String redirectUrl =
                "http://localhost:5173/oauth-success" +
                        "?token=" + URLEncoder.encode(jwtResponse.getToken(), StandardCharsets.UTF_8) +
                        "&role=" + URLEncoder.encode(jwtResponse.getRole(), StandardCharsets.UTF_8) +
                        "&email=" + URLEncoder.encode(jwtResponse.getEmail(), StandardCharsets.UTF_8);

        response.sendRedirect(redirectUrl);
        clearAuthenticationAttributes(request);
    }
}
