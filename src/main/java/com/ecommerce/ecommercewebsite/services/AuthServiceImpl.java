package com.ecommerce.ecommercewebsite.services;

import com.ecommerce.ecommercewebsite.dto.*;
import com.ecommerce.ecommercewebsite.enums.AuthErrorCode;
import com.ecommerce.ecommercewebsite.exception.*;
import com.ecommerce.ecommercewebsite.model.Role;
import com.ecommerce.ecommercewebsite.model.User;
import com.ecommerce.ecommercewebsite.repositories.RoleRepository;
import com.ecommerce.ecommercewebsite.repositories.UserRepository;
import com.ecommerce.ecommercewebsite.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EmailService emailService;


    @Override
    public JwtResponseDto loginUser(LoginRequestDTO request) {
        // extract  the use from the database
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(AuthErrorCode.INVALID_CREDENTIALS));
        String role = user.getRole().getRole();
        // check verified
        if (!user.isVerified()) {
            throw new ApiException(AuthErrorCode.NOT_VERIFIED);
        }
        try {
            // authenticate  the  user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new ApiException(AuthErrorCode.INVALID_CREDENTIALS);
        }
        String token = jwtUtil.generateToken(request.getEmail(), role);
        return new JwtResponseDto(request.getEmail(), token, role);

    }

    @Override
    public String registerUser(@Valid RegisterRequestDTO request, MultipartFile profile) {
        //  check if  exist in the database or not
        User existingUser = userRepository.findByEmail(request.getEmail()).orElse(null);
        //  Case 1: User already exists and verified
        if (existingUser != null && existingUser.isVerified()) {
            throw new ApiException(AuthErrorCode.USER_ALREADY_EXISTS);
        }
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        // case  2  :  if  user  exist   but  not  verified
        if (existingUser != null && !existingUser.isVerified()) {
            existingUser.setOtp(otp);
            existingUser.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
            userRepository.save(existingUser);
            EmailDetailsDTO emailDetailsDTO = new EmailDetailsDTO(
                    existingUser.getEmail(),
                    "Hello " + existingUser.getName() +
                            ",Your verification code(otp) is \n" + otp + "\n\n It  will expire in 10 minutes"
                    , "Verify your email."
            );
            emailService.sendSimpleMail(emailDetailsDTO);
            return "OTP_RESENT: OTP sent to your email. Please verify.";
        }
//  case3: new user registration
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCity(request.getCity());
        user.setNumber(request.getNumber());
        try {
            user.setProfile(profile.getBytes());
        } catch (Exception e) {
            throw new ImagenNotFoundException("Profile not found");
        }

        Role role = roleRepository.findByRole("ROLE_USER");
        user.setRole(role); //You set the Role object in Java, but JPA stores only the role_id in the database.
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

        // save to the database
        User savedUser = userRepository.save(user);
        System.out.println("User is Saved in the database");

// Send Registration Email
        EmailDetailsDTO mail = new EmailDetailsDTO(
                savedUser.getEmail(),
                "Hello " + savedUser.getName() +
                        ",Your verification code(otp) is \n" + otp + "\n\n It  will expire in 10 minutes"
                , "Verify your email."
        );

        emailService.sendSimpleMail(mail);
        return "OTP sent to your email. Please verify.";
    }

    @Override
    public String verifyOtp(OtpVerifyDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user.isVerified()) {
            return "User is already verified";
        }
        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            return "Otp is expired";
        }

        // check otp matches
        if (request.getOtp().equals(user.getOtp())) {
            user.setVerified(true);
            user.setOtp(null);
            user.setOtpExpiry(null);
            userRepository.save(user);
            return "Otp has been verified";
        } else {
            return "Invalid Otp";
        }


    }

    @Override
    public String changePassword(String email, ChangePasswordDTO change) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        //  check   old  password
        if (!passwordEncoder.matches(change.getOldPassword(), user.getPassword())) {
            throw new PasswordException("Passwords don't match");
        }
        // prevent same  password reuse
        if (passwordEncoder.matches(change.getNewPassword(), user.getPassword())) {
            throw new PasswordException("New Password  must be different from old  password ");
        }
        user.setPassword(passwordEncoder.encode(change.getNewPassword()));
        userRepository.save(user);
        return "Password has been changed";
    }

    @Override
    public String sendForgetPasswordOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        User savedUser = userRepository.save(user);
        // Send Registration Email
        EmailDetailsDTO mail = new EmailDetailsDTO(
                savedUser.getEmail(),
                "Hello " + savedUser.getName() +
                        ",Your  password reset code(otp) is" + otp + "\n\n It  will expire in 10 minutes"
                , "Verify your otp."
        );

        emailService.sendSimpleMail(mail);  // 🔥 Trigger email
        return "OTP sent to your email. Please verify.";


    }

    @Override
    public String resetPassword(ResetPasswordDTO request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // check  otp
        if (!request.getOtp().equals(user.getOtp())) {
            throw new OtpVerificationException("Invalid Otp");
        }
        // check otp expiry
        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new OtpVerificationException("Invalid Expired");
        }


        // check new password same as old
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new PasswordException("New password cannot be same as old password");
        }
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {

            throw new PasswordException("New passwords don't match");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setVerified(true);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        return "Password has been reset";

    }

}
