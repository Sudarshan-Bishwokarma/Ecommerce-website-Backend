package com.ecommerce.ecommercewebsite.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    @Email
    private String email;
    @Size(min = 6)
    private String password;

    private String otp;
    private LocalDateTime otpExpiry;
    private boolean isVerified = false;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @OneToOne
    private Profile profile;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private BusinessProfile businessProfile;
}
