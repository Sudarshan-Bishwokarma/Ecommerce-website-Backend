package com.ecommerce.ecommercewebsite.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String email;
    private String password;
    private String city;
    private String number;
    @Lob //@Lob tells JPA to store large data
    @Column(columnDefinition = "LONGBLOB")
    private byte[] profile;
    private String otp;
    private boolean isVerified = false; // indicates whether OTP verification is done
    private LocalDateTime otpExpiry; // optional, OTP expires after X minutes

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
