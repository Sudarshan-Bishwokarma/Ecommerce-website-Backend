package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.EmailDetailsDTO;
import com.ecommerce.ecommercewebsite.model.User;

public interface EmailService {
    public String sendSimpleMail(EmailDetailsDTO details);

}
