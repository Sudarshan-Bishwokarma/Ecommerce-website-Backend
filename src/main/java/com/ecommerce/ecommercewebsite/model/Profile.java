package com.ecommerce.ecommercewebsite.model;

import com.ecommerce.ecommercewebsite.dto.ProfileRequestDTO;
import com.ecommerce.ecommercewebsite.enums.ProfileStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private String country;
    private String number;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] profileImage;
    @Enumerated(EnumType.STRING)
    private ProfileStatus profileStatus = ProfileStatus.PENDING;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
