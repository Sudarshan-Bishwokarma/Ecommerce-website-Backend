package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.EmailDetailsDTO;
import com.ecommerce.ecommercewebsite.dto.JwtResponseDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

public interface OAuth2UserService {
    public JwtResponseDto processGoogleLogin (OAuth2User oAuth2User);


}
